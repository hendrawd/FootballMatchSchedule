package ganteng.hendrawd.footballmatchschedule.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.gson.Gson
import ganteng.hendrawd.footballmatchschedule.network.EndPoint
import ganteng.hendrawd.footballmatchschedule.network.getDataAsString
import ganteng.hendrawd.footballmatchschedule.network.model.GetAllLeaguesResponse
import ganteng.hendrawd.footballmatchschedule.common.util.toLeagueModelList
import ganteng.hendrawd.footballmatchschedule.view.model.LeagueModel
import kotlinx.coroutines.experimental.launch

/**
 * @author hendrawd on 06/08/18
 */
class LeagueListViewModel : ViewModel() {
    private val mObservableLeagues: MutableLiveData<List<LeagueModel>> = MutableLiveData()
    private var leaguesCache: List<LeagueModel>? = null
    private var selectedLeague: LeagueModel? = null

    fun getLeaguesObservable(): LiveData<List<LeagueModel>> {
        return mObservableLeagues
    }

    fun loadLeagues() {
        launch {
            if (leaguesCache == null) {
                val jsonString = EndPoint.allLeagues().getDataAsString
                val getAllLeaguesResponse = Gson().fromJson(jsonString, GetAllLeaguesResponse::class.java)
                leaguesCache = getAllLeaguesResponse.leagueList.toLeagueModelList()
            }
            mObservableLeagues.postValue(leaguesCache)
        }
    }

    fun setSelectedLeague(league: LeagueModel) {
        selectedLeague = league
    }

    fun getSelectedLeague(): LeagueModel? {
        return selectedLeague
    }
}