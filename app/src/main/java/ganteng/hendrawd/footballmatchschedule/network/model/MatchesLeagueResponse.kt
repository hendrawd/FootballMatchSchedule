package ganteng.hendrawd.footballmatchschedule.network.model

import com.google.gson.annotations.SerializedName

/**
 * @author hendrawd on 19/07/18
 */
data class MatchesLeagueResponse(
        @SerializedName("events")
        var matchList: List<MatchNetworkModel>?
)