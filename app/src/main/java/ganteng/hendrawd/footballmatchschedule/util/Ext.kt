package ganteng.hendrawd.footballmatchschedule.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.widget.ImageView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import ganteng.hendrawd.footballmatchschedule.R
import ganteng.hendrawd.footballmatchschedule.db.FavoriteMatch
import ganteng.hendrawd.footballmatchschedule.db.FavoriteTeam
import ganteng.hendrawd.footballmatchschedule.network.model.LeagueNetworkModel
import ganteng.hendrawd.footballmatchschedule.network.model.MatchNetworkModel
import ganteng.hendrawd.footballmatchschedule.network.model.PlayerNetworkModel
import ganteng.hendrawd.footballmatchschedule.network.model.TeamNetworkModel
import ganteng.hendrawd.footballmatchschedule.view.model.*

fun List<MatchNetworkModel>.networkModelListToMatchModelList(): List<MatchModel> {
    val matchList = mutableListOf<MatchModel>()
    for (matchNetworkModel in this) {
        matchList.add(matchNetworkModel.toMatchModel())
    }
    return matchList
}

fun MatchNetworkModel.toMatchModel(): MatchModel {
    return MatchModel(idEvent ?: "",
            toHomeTeamStatisticsModel(),
            toAwayTeamStatisticsModel(),
            dateEvent ?: "",
            strTime ?: ""
    )
}

fun MatchNetworkModel.toHomeTeamStatisticsModel(): TeamStatisticsModel {
    return TeamStatisticsModel(
            idHomeTeam ?: "",
            strHomeTeam ?: "",
            intHomeScore ?: "",
            strHomeGoalDetails ?: "",
            strHomeRedCards ?: "",
            strHomeYellowCards ?: "",
            strHomeLineupGoalkeeper ?: "",
            strHomeLineupDefense ?: "",
            strHomeLineupMidfield ?: "",
            strHomeLineupForward ?: "",
            strHomeLineupSubstitutes ?: "",
            strHomeFormation ?: ""
    )
}

fun MatchNetworkModel.toAwayTeamStatisticsModel(): TeamStatisticsModel {
    return TeamStatisticsModel(
            idAwayTeam ?: "",
            strAwayTeam ?: "",
            intAwayScore ?: "",
            strAwayGoalDetails ?: "",
            strAwayRedCards ?: "",
            strAwayYellowCards ?: "",
            strAwayLineupGoalkeeper ?: "",
            strAwayLineupDefense ?: "",
            strAwayLineupMidfield ?: "",
            strAwayLineupForward ?: "",
            strAwayLineupSubstitutes ?: "",
            strAwayFormation ?: ""
    )
}

fun List<TeamNetworkModel>.toTeamModelList(): List<TeamModel> {
    val teamList = mutableListOf<TeamModel>()
    for (teamNetworkModel in this) {
        teamList.add(teamNetworkModel.toTeamModel())
    }
    return teamList
}

fun TeamNetworkModel.toTeamModel(): TeamModel {
    return TeamModel(
            idTeam ?: "",
            strTeamLogo ?: "",
            strTeamBanner ?: "",
            strTeam ?: "",
            strDescriptionEN ?: "",
            intFormedYear ?: "",
            strStadium ?: ""
    )
}

fun List<MatchModel>.toFavoriteMatchListModel(): List<FavoriteMatch> {
    val favoriteMatchList = mutableListOf<FavoriteMatch>()
    for (matchModel in this) {
        favoriteMatchList.add(matchModel.toFavoriteMatch())
    }
    return favoriteMatchList
}

fun List<FavoriteMatch>.favoriteMatchListToMatchModelList(): List<MatchModel> {
    val matchModelList = mutableListOf<MatchModel>()
    for (favoriteMatch in this) {
        matchModelList.add(favoriteMatch.toMatchModel())
    }
    return matchModelList
}

fun List<FavoriteTeam>.favoriteTeamToTeamModelList(): List<TeamModel> {
    val teamModelList = mutableListOf<TeamModel>()
    for (favoriteTeam in this) {
        teamModelList.add(favoriteTeam.toTeamModel())
    }
    return teamModelList
}

fun MatchModel.toFavoriteMatch(): FavoriteMatch {
    val gson = Gson()
    return FavoriteMatch(
            id,
            gson.toJson(team1Statistics),
            gson.toJson(team2Statistics),
            date,
            time
    )
}

fun FavoriteMatch.toMatchModel(): MatchModel {
    val gson = Gson()
    return MatchModel(
            id,
            gson.fromJson(team1Statistics, TeamStatisticsModel::class.java),
            gson.fromJson(team2Statistics, TeamStatisticsModel::class.java),
            date,
            time
    )
}

fun TeamModel.toFavoriteTeam(): FavoriteTeam {
    return FavoriteTeam(
            id,
            logoUrl,
            backdropUrl,
            name,
            description,
            formedYear,
            stadium
    )
}

fun FavoriteTeam.toTeamModel(): TeamModel {
    return TeamModel(
            id,
            logoUrl,
            backdropUrl,
            name,
            description,
            formedYear,
            stadium
    )
}

fun LeagueNetworkModel.toLeagueModel(): LeagueModel {
    return LeagueModel(idLeague, strLeague)
}

fun PlayerNetworkModel.toPlayerModel(): PlayerModel {
    return PlayerModel(
            idPlayer ?: "",
            strCutout ?: strThumb ?: "",
            strFanart1 ?: "",
            strPlayer ?: "",
            strDescriptionEN ?: "",
            strWeight ?: "",
            strHeight ?: "",
            strPosition ?: ""
    )
}

fun List<LeagueNetworkModel>.toLeagueModelList(): List<LeagueModel> {
    val leagueModelList = mutableListOf<LeagueModel>()
    for (leagueNetworkModel in this) {
        leagueModelList.add(leagueNetworkModel.toLeagueModel())
    }
    return leagueModelList
}

fun List<PlayerNetworkModel>.toPlayerModelArrayList(): ArrayList<PlayerModel> {
    val playerModelArrayList = arrayListOf<PlayerModel>()
    for (playerNetworkModel in this) {
        playerModelArrayList.add(playerNetworkModel.toPlayerModel())
    }
    return playerModelArrayList
}

fun ImageView.load(url: String) {
    if (TextUtils.isEmpty(url)) {
        scaleType = ImageView.ScaleType.FIT_XY
        layoutParams.height = width
        setImageResource(R.drawable.ic_broken_image)
        return
    }
    Picasso.get()
            .load(url)
            .placeholder(R.drawable.ic_progress_animation)
            .error(R.drawable.ic_broken_image)
            .into(this)
}

/**
 * Copied and modified from
 * https://stackoverflow.com/questions/8276634/android-get-hosting-activity-from-a-view/32973351#32973351
 *
 * @return Activity
 */
fun Context.getActivity(): Activity? {
    var nextContext = this
    while (nextContext is ContextWrapper) {
        if (nextContext is Activity) {
            return nextContext
        }
        nextContext = nextContext.baseContext
    }
    return null
}

inline fun <reified T : Any> Activity.launchActivity(
        requestCode: Int = -1,
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}) {

    val intent = newIntent<T>(this)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivityForResult(intent, requestCode, options)
    } else {
        startActivityForResult(intent, requestCode)
    }
}

inline fun <reified T : Any> Context.launchActivity(
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}) {

    val intent = newIntent<T>(this)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivity(intent, options)
    } else {
        startActivity(intent)
    }
}

inline fun <reified T : Any> newIntent(context: Context): Intent =
        Intent(context, T::class.java)