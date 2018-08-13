package ganteng.hendrawd.footballmatchschedule.network

import android.net.Uri
import ganteng.hendrawd.footballmatchschedule.BuildConfig

/**
 * An helper object for building end point urls
 * @author hendrawd on 19/07/18
 */
object EndPoint {
    /**
     * Get base api url Uri.Builder
     * @return Uri.Builder of base api url
     */
    private fun baseApiUrl(): Uri.Builder {
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
                .appendPath("api")
                .appendPath("v1")
                .appendPath("json")
                .appendPath(BuildConfig.TSDB_API_KEY)
    }

    /**
     * Get all leagues url
     * i.e: https://www.thesportsdb.com/api/v1/json/1/all_leagues.php
     * @return String all leagues url
     */
    internal fun allLeagues(): String {
        return baseApiUrl()
                .appendPath("all_leagues.php")
                .build()
                .toString()
    }

    /**
     * Get Next 15 Matches by League Id url
     * i.e: https://www.thesportsdb.com/api/v1/json/1/eventsnextleague.php?id=4328
     * @return String next15MatchesByLeagueId url
     */
    internal fun next15MatchesByLeagueId(leagueId: String): String {
        return baseApiUrl()
                .appendPath("eventsnextleague.php")
                .appendQueryParameter("id", leagueId)
                .build()
                .toString()
    }

    /**
     * Get Last 15 Matches by League Id url
     * i.e: https://www.thesportsdb.com/api/v1/json/1/eventspastleague.php?id=4328
     * @return String last15MatchesByLeagueId url
     */
    internal fun last15MatchesByLeagueId(leagueId: String): String {
        return baseApiUrl()
                .appendPath("eventspastleague.php")
                .appendQueryParameter("id", leagueId)
                .build()
                .toString()
    }

    /**
     * Get match detail by match id
     * Note: from the list last15MatchesByLeagueId and next15MatchesByLeagueId is enough, because the information is already detailed
     * i.e: https://www.thesportsdb.com/api/v1/json/1/lookupevent.php?id=526919
     * @return String getMatchDetailById url
     */
    internal fun matchDetailById(matchId: String): String {
        return baseApiUrl()
                .appendPath("lookupevent.php")
                .appendQueryParameter("id", matchId)
                .build()
                .toString()
    }

    /**
     * Get List all Teams in a League url
     * i.e: https://www.thesportsdb.com/api/v1/json/1/search_all_teams.php?l=English%20Premier%20League
     * @return String listTeamsByLeague url
     */
    internal fun listTeamsByLeague(league: String): String {
        return baseApiUrl()
                .appendPath("search_all_teams.php")
                .appendQueryParameter("l", league)
                .build()
                .toString()
    }

    /**
     * Get team by id
     * i.e: https://www.thesportsdb.com/api/v1/json/1/lookupteam.php?id=133604
     * @return String team url
     */
    internal fun getTeam(teamId: String): String {
        return baseApiUrl()
                .appendPath("lookupteam.php")
                .appendQueryParameter("id", teamId)
                .build()
                .toString()
    }

    /**
     * List All players in a team by Team Id
     * i.e: https://www.thesportsdb.com/api/v1/json/1/lookup_all_players.php?id=133604
     * @return String url to get all players in a team by team id
     */
    internal fun getPlayers(teamId: String): String {
        return baseApiUrl()
                .appendPath("lookup_all_players.php")
                .appendQueryParameter("id", teamId)
                .build()
                .toString()
    }
}