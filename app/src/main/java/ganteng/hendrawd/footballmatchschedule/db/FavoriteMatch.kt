package ganteng.hendrawd.footballmatchschedule.db

data class FavoriteMatch(val id: String, val team1Statistics: String, val team2Statistics: String, val date: String, val time: String) {

    object Column {
        const val ID: String = "MATCH_ID"
        const val TEAM_1_STATISTICS: String = "TEAM_1_STATISTICS"
        const val TEAM_2_STATISTICS: String = "TEAM_2_STATISTICS"
        const val DATE: String = "DATE"
        const val TIME: String = "TIME"
    }

    companion object {
        const val TABLE_NAME: String = "TABLE_FAVORITE_MATCH"
    }
}