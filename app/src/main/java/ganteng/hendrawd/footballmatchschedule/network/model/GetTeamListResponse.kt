package ganteng.hendrawd.footballmatchschedule.network.model

import com.google.gson.annotations.SerializedName

/**
 * @author hendrawd on 30/07/18
 */
data class GetTeamListResponse(
        @SerializedName("teams")
        var teamList: List<TeamNetworkModel>?
)