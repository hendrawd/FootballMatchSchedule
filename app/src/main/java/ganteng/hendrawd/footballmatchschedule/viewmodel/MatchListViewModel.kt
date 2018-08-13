package ganteng.hendrawd.footballmatchschedule.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.text.TextUtils
import com.google.gson.Gson
import ganteng.hendrawd.footballmatchschedule.db.FavoriteMatch
import ganteng.hendrawd.footballmatchschedule.db.database
import ganteng.hendrawd.footballmatchschedule.network.EndPoint
import ganteng.hendrawd.footballmatchschedule.network.getDataAsString
import ganteng.hendrawd.footballmatchschedule.network.model.MatchesLeagueResponse
import ganteng.hendrawd.footballmatchschedule.util.favoriteMatchListToMatchModelList
import ganteng.hendrawd.footballmatchschedule.util.networkModelListToMatchModelList
import ganteng.hendrawd.footballmatchschedule.view.model.LeagueModel
import ganteng.hendrawd.footballmatchschedule.view.model.MatchModel
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select


/**
 * @author hendrawd on 19/07/18
 * Example source for implementing ViewModel from
 * https://github.com/googlesamples/android-architecture-components/blob/master/BasicSample/app/src/main/java/com/example/android/persistence/viewmodel/ProductListViewModel.java
 */
class MatchListViewModel(application: Application) : AndroidViewModel(application) {
    private var league: LeagueModel? = null
    private val mObservableMatches: MutableLiveData<List<MatchModel>> = MutableLiveData()
    private val database = application.database
    private var previousMatchesCache: List<MatchModel>? = null
    private var nextMatchesCache: List<MatchModel>? = null

    fun getMatchesObservable(): LiveData<List<MatchModel>> {
        return mObservableMatches
    }

    fun setLeague(league: LeagueModel) {
        this.league = league
    }

    fun getLeague(): LeagueModel {
        return league!!
    }

    fun loadPreviousMatches(query: String? = null, refresh: Boolean = false) {
        league?.let {
            launch {
                if (previousMatchesCache == null || refresh) {
                    val jsonString = EndPoint.last15MatchesByLeagueId(it.id).getDataAsString
                    val matchesPastLeagueResponse = Gson().fromJson(jsonString, MatchesLeagueResponse::class.java)
                    previousMatchesCache = matchesPastLeagueResponse.matchList?.networkModelListToMatchModelList()
                }
                postMatchList(previousMatchesCache ?: listOf(), query)
            }
        }
    }

    fun loadNextMatches(query: String? = null, refresh: Boolean = false) {
        league?.let {
            launch {
                if (nextMatchesCache == null || refresh) {
                    val jsonString = EndPoint.next15MatchesByLeagueId(it.id).getDataAsString
                    val matchesNextLeagueResponse = Gson().fromJson(jsonString, MatchesLeagueResponse::class.java)
                    nextMatchesCache = matchesNextLeagueResponse.matchList?.networkModelListToMatchModelList()
                }
                postMatchList(nextMatchesCache ?: listOf(), query)
            }
        }
    }

    fun loadFavoriteMatches(query: String? = null) {
        database.use {
            val queryBuilder = select(FavoriteMatch.TABLE_NAME)
            val favoriteMatchList = queryBuilder.parseList(classParser<FavoriteMatch>())
            val matchList = favoriteMatchList.favoriteMatchListToMatchModelList()
            // TODO filter by select with where condition query directly
            postMatchList(matchList, query)
        }
    }

    private fun postMatchList(matchList: List<MatchModel>, query: String?) {
        if (!TextUtils.isEmpty(query)) {
            mObservableMatches.postValue(filterResult(matchList, query!!))
        } else {
            mObservableMatches.postValue(matchList)
        }
    }

    private fun filterResult(matchList: List<MatchModel>, query: String): List<MatchModel> {
        return matchList.filter {
            it.team1Statistics.name.toLowerCase().contains(query) ||
                    it.team2Statistics.name.toLowerCase().contains(query)
        }
    }
}