package ganteng.hendrawd.footballmatchschedule.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.gson.Gson
import ganteng.hendrawd.footballmatchschedule.network.EndPoint
import ganteng.hendrawd.footballmatchschedule.network.getDataAsString
import ganteng.hendrawd.footballmatchschedule.network.model.GetPlayerListResponse
import ganteng.hendrawd.footballmatchschedule.common.util.toPlayerModelArrayList
import ganteng.hendrawd.footballmatchschedule.view.model.PlayerModel
import kotlinx.coroutines.experimental.launch

/**
 * @author hendrawd on 09/08/18
 */
class PlayerListViewModel : ViewModel() {
    private val observablePlayerList: MutableLiveData<ArrayList<PlayerModel>> = MutableLiveData()
    private var playerListCache: ArrayList<PlayerModel>? = null
    private var teamId: String? = null

    fun setTeamId(teamId: String) {
        this.teamId = teamId
    }

    fun getTeamId(): String? {
        return teamId
    }

    fun getPlayerListObservable(): LiveData<ArrayList<PlayerModel>> {
        return observablePlayerList
    }

    fun loadPlayerList(refresh: Boolean = false) {
        teamId?.let {
            launch {
                if (playerListCache == null || refresh) {
                    val jsonString = EndPoint.getPlayers(it).getDataAsString
                    val getPlayerListResponse = Gson().fromJson(jsonString, GetPlayerListResponse::class.java)
                    playerListCache = getPlayerListResponse.playerList?.toPlayerModelArrayList()
                }
                observablePlayerList.postValue(playerListCache)
            }
        }
    }
}