package ganteng.hendrawd.footballmatchschedule.network.model

data class MatchNetworkModel(
        var idEvent: String? = null,
        var idSoccerXML: String? = null,
        var strEvent: String? = null,
        var strFilename: String? = null,
        var strSport: String? = null,
        var idLeague: String? = null,
        var strLeague: String? = null,
        var strSeason: String? = null,
        var strDescriptionEN: String? = null,
        var strHomeTeam: String? = null,
        var strAwayTeam: String? = null,
        var intHomeScore: String? = null,
        var intRound: String? = null,
        var intAwayScore: String? = null,
        var intSpectators: String? = null,
        var strHomeGoalDetails: String? = null,
        var strHomeRedCards: String? = null,
        var strHomeYellowCards: String? = null,
        var strHomeLineupGoalkeeper: String? = null,
        var strHomeLineupDefense: String? = null,
        var strHomeLineupMidfield: String? = null,
        var strHomeLineupForward: String? = null,
        var strHomeLineupSubstitutes: String? = null,
        var strHomeFormation: String? = null,
        var strAwayRedCards: String? = null,
        var strAwayYellowCards: String? = null,
        var strAwayGoalDetails: String? = null,
        var strAwayLineupGoalkeeper: String? = null,
        var strAwayLineupDefense: String? = null,
        var strAwayLineupMidfield: String? = null,
        var strAwayLineupForward: String? = null,
        var strAwayLineupSubstitutes: String? = null,
        var strAwayFormation: String? = null,
        var intHomeShots: String? = null,
        var intAwayShots: String? = null,
        var dateEvent: String? = null,
        var strDate: String? = null,
        var strTime: String? = null,
        var strTVStation: String? = null,
        var idHomeTeam: String? = null,
        var idAwayTeam: String? = null,
        var strResult: String? = null,
        var strCircuit: String? = null,
        var strCountry: String? = null,
        var strCity: String? = null,
        var strPoster: String? = null,
        var strFanart: String? = null,
        var strThumb: String? = null,
        var strBanner: String? = null,
        var strMap: String? = null,
        var strLocked: String? = null
)