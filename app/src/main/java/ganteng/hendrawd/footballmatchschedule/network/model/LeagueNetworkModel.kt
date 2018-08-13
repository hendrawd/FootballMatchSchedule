package ganteng.hendrawd.footballmatchschedule.network.model

/**
 * @author hendrawd on 06/08/18
 */
data class LeagueNetworkModel(
        val idLeague: String,
        val strLeague: String,
        val strSport: String,
        val strLeagueAlternate: String
) {
    override fun toString(): String {
        return strLeague
    }
}