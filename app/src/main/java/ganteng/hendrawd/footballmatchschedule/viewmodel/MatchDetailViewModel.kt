package ganteng.hendrawd.footballmatchschedule.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.database.sqlite.SQLiteConstraintException
import ganteng.hendrawd.footballmatchschedule.db.FavoriteMatch
import ganteng.hendrawd.footballmatchschedule.db.MyDatabaseOpenHelper
import ganteng.hendrawd.footballmatchschedule.db.database
import ganteng.hendrawd.footballmatchschedule.common.util.toFavoriteMatch
import ganteng.hendrawd.footballmatchschedule.view.model.MatchModel
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

class MatchDetailViewModel(application: Application) : AndroidViewModel(application) {

    var shouldRefreshFavoriteMatch = false
    private val database: MyDatabaseOpenHelper = application.database
    private val observableMatch: MutableLiveData<MatchModel> = MutableLiveData()
    private val observableFavoriteMatch: MutableLiveData<FavoriteMatch> = MutableLiveData()

    fun getMatchObservable(): LiveData<MatchModel> {
        return observableMatch
    }

    fun getFavoriteMatchObservable(): LiveData<FavoriteMatch> {
        return observableFavoriteMatch
    }

    fun setMatch(match: MatchModel) {
        observableMatch.value = match
        loadFavoriteMatch()
    }

    fun toggleFavoriteMatch() {
        if (isFavoriteMatch()) {
            deleteFromFavorite()
        } else {
            insertToFavorite()
        }
        shouldRefreshFavoriteMatch = !shouldRefreshFavoriteMatch
    }

    private fun loadFavoriteMatch() {
        observableMatch.value?.also {
            database.use {
                val result = select(FavoriteMatch.TABLE_NAME)
                        .whereArgs(
                                "${FavoriteMatch.Column.ID} = {id}",
                                "id" to it.id
                        )
                val favoriteMatch = result.parseOpt(classParser<FavoriteMatch>())
                observableFavoriteMatch.postValue(favoriteMatch)
            }
        }
    }

    private fun isFavoriteMatch(): Boolean {
        return observableFavoriteMatch.value != null
    }

    private fun insertToFavorite() {
        val match = observableMatch.value
        match?.also {
            val favoriteMatch = it.toFavoriteMatch()
            try {
                database.use {
                    insert(
                            FavoriteMatch.TABLE_NAME,
                            FavoriteMatch.Column.ID to favoriteMatch.id,
                            FavoriteMatch.Column.TEAM_1_STATISTICS to favoriteMatch.team1Statistics,
                            FavoriteMatch.Column.TEAM_2_STATISTICS to favoriteMatch.team2Statistics,
                            FavoriteMatch.Column.DATE to favoriteMatch.date,
                            FavoriteMatch.Column.TIME to favoriteMatch.time
                    )
                    observableFavoriteMatch.postValue(favoriteMatch)
                }
            } catch (e: SQLiteConstraintException) {
            }
        }
    }

    private fun deleteFromFavorite() {
        val match = observableMatch.value
        match?.also {
            try {
                database.use {
                    delete(
                            FavoriteMatch.TABLE_NAME,
                            "${FavoriteMatch.Column.ID} = {id}",
                            "id" to it.id
                    )
                    observableFavoriteMatch.postValue(null)
                }
            } catch (e: SQLiteConstraintException) {
            }
        }
    }
}