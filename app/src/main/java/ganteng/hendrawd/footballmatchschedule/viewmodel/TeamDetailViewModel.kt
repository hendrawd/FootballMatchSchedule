package ganteng.hendrawd.footballmatchschedule.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.database.sqlite.SQLiteConstraintException
import ganteng.hendrawd.footballmatchschedule.db.FavoriteTeam
import ganteng.hendrawd.footballmatchschedule.db.MyDatabaseOpenHelper
import ganteng.hendrawd.footballmatchschedule.db.database
import ganteng.hendrawd.footballmatchschedule.util.toFavoriteTeam
import ganteng.hendrawd.footballmatchschedule.view.model.TeamModel
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

/**
 * @author hendrawd on 30/07/18
 */
class TeamDetailViewModel(application: Application) : AndroidViewModel(application) {

    var shouldRefreshFavoriteTeam = false
    private val database: MyDatabaseOpenHelper = application.database
    private var team: TeamModel? = null
    private val observableFavoriteTeam: MutableLiveData<FavoriteTeam> = MutableLiveData()

    fun getTeam(): TeamModel? {
        return team
    }

    fun setTeam(teamModel: TeamModel) {
        team = teamModel
        loadFavoriteTeam()
    }

    fun getFavoriteTeamObservable(): LiveData<FavoriteTeam> {
        return observableFavoriteTeam
    }

    fun toggleFavoriteMatch() {
        if (isFavoriteTeam()) {
            deleteFromFavorite()
        } else {
            insertToFavorite()
        }
        shouldRefreshFavoriteTeam = !shouldRefreshFavoriteTeam
    }

    private fun loadFavoriteTeam() {
        team?.also {
            database.use {
                val result = select(FavoriteTeam.TABLE_NAME)
                        .whereArgs(
                                "${FavoriteTeam.Column.ID} = {id}",
                                "id" to it.id
                        )
                val favoriteTeam = result.parseOpt(classParser<FavoriteTeam>())
                observableFavoriteTeam.postValue(favoriteTeam)
            }
        }
    }

    private fun isFavoriteTeam(): Boolean {
        return observableFavoriteTeam.value != null
    }

    private fun insertToFavorite() {
        team?.also {
            val favoriteTeam = it.toFavoriteTeam()
            try {
                database.use {
                    insert(
                            FavoriteTeam.TABLE_NAME,
                            FavoriteTeam.Column.ID to favoriteTeam.id,
                            FavoriteTeam.Column.LOGO_URL to favoriteTeam.logoUrl,
                            FavoriteTeam.Column.BACKDROP_URL to favoriteTeam.backdropUrl,
                            FavoriteTeam.Column.NAME to favoriteTeam.name,
                            FavoriteTeam.Column.DESCRIPTION to favoriteTeam.description,
                            FavoriteTeam.Column.FORMED_YEAR to favoriteTeam.formedYear,
                            FavoriteTeam.Column.STADIUM to favoriteTeam.stadium
                    )
                    observableFavoriteTeam.postValue(favoriteTeam)
                }
            } catch (e: SQLiteConstraintException) {
            }
        }
    }

    private fun deleteFromFavorite() {
        team?.also {
            try {
                database.use {
                    delete(
                            FavoriteTeam.TABLE_NAME,
                            "${FavoriteTeam.Column.ID} = {id}",
                            "id" to it.id
                    )
                    observableFavoriteTeam.postValue(null)
                }
            } catch (e: SQLiteConstraintException) {
            }
        }
    }
}

