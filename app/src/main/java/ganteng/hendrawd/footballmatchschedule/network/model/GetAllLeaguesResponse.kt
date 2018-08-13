package ganteng.hendrawd.footballmatchschedule.network.model

import com.google.gson.annotations.SerializedName

/**
 * @author hendrawd on 06/08/18
 */
data class GetAllLeaguesResponse(
        @SerializedName("leagues")
        var leagueList: List<LeagueNetworkModel>
)