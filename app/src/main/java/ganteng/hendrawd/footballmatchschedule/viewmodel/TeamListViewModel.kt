package ganteng.hendrawd.footballmatchschedule.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.text.TextUtils
import com.google.gson.Gson
import ganteng.hendrawd.footballmatchschedule.db.FavoriteTeam
import ganteng.hendrawd.footballmatchschedule.db.database
import ganteng.hendrawd.footballmatchschedule.network.EndPoint
import ganteng.hendrawd.footballmatchschedule.network.getDataAsString
import ganteng.hendrawd.footballmatchschedule.network.model.GetTeamListResponse
import ganteng.hendrawd.footballmatchschedule.util.favoriteTeamToTeamModelList
import ganteng.hendrawd.footballmatchschedule.util.toTeamModelList
import ganteng.hendrawd.footballmatchschedule.view.model.LeagueModel
import ganteng.hendrawd.footballmatchschedule.view.model.TeamModel
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select

/**
 * @author hendrawd on 19/07/18
 * Example source for implementing ViewModel from
 * https://github.com/googlesamples/android-architecture-components/blob/master/BasicSample/app/src/main/java/com/example/android/persistence/viewmodel/ProductListViewModel.java
 */
class TeamListViewModel(application: Application) : AndroidViewModel(application) {
    private var league: LeagueModel? = null
    private val mObservableTeams: MutableLiveData<List<TeamModel>> = MutableLiveData()
    private val database = application.database
    private var teamsCache: List<TeamModel>? = null

    fun getTeamsObservable(): LiveData<List<TeamModel>> {
        return mObservableTeams
    }

    fun setLeague(league: LeagueModel) {
        this.league = league
    }

    fun getLeague(): LeagueModel {
        return league!!
    }

    fun loadTeams(query: String? = null, refresh: Boolean = false) {
        league?.let {
            launch {
                if (teamsCache == null || refresh) {
                    val jsonString = EndPoint.listTeamsByLeague(it.name).getDataAsString
                    val listTeamByLeagueResponse = Gson().fromJson(jsonString, GetTeamListResponse::class.java)
                    teamsCache = listTeamByLeagueResponse.teamList?.toTeamModelList()
                }
                postTeamList(teamsCache ?: listOf(), query)
            }
        }
    }

    fun loadFavoriteTeams(query: String? = null) {
        database.use {
            val queryBuilder = select(FavoriteTeam.TABLE_NAME)
            val favoriteTeamList = queryBuilder.parseList(classParser<FavoriteTeam>())
            val teamList = favoriteTeamList.favoriteTeamToTeamModelList()
            // TODO filter by select with where condition query directly
            postTeamList(teamList, query)
        }
    }

    private fun postTeamList(teamList: List<TeamModel>, query: String?) {
        if (!TextUtils.isEmpty(query)) {
            mObservableTeams.postValue(filterResult(teamList, query!!))
        } else {
            mObservableTeams.postValue(teamList)
        }
    }

    private fun filterResult(teamList: List<TeamModel>, query: String): List<TeamModel> {
        return teamList.filter {
            it.name.toLowerCase().contains(query)
        }
    }
}