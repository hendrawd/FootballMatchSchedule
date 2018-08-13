package ganteng.hendrawd.footballmatchschedule.db

data class FavoriteTeam(val id: String, val logoUrl: String, val backdropUrl: String, val name: String, val description: String, val formedYear: String, val stadium: String) {

    object Column {
        const val ID: String = "TEAM_ID"
        const val LOGO_URL: String = "LOGO_URL"
        const val BACKDROP_URL: String = "BACKDROP_URL"
        const val NAME: String = "NAME"
        const val DESCRIPTION: String = "DESCRIPTION"
        const val FORMED_YEAR: String = "FORMED_YEAR"
        const val STADIUM: String = "STADIUM"
    }

    companion object {
        const val TABLE_NAME: String = "TABLE_FAVORITE_TEAM"
    }
}