package ganteng.hendrawd.footballmatchschedule.network.model

import com.google.gson.annotations.SerializedName

/**
 * @author hendrawd on 09/08/18
 */
data class GetPlayerListResponse(
        @SerializedName("player")
        var playerList: List<PlayerNetworkModel>?
)