package ganteng.hendrawd.footballmatchschedule.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.gson.Gson
import ganteng.hendrawd.footballmatchschedule.network.EndPoint
import ganteng.hendrawd.footballmatchschedule.network.getDataAsString
import ganteng.hendrawd.footballmatchschedule.network.model.GetTeamListResponse
import ganteng.hendrawd.footballmatchschedule.util.toTeamModel
import ganteng.hendrawd.footballmatchschedule.view.model.TeamModel
import org.jetbrains.anko.doAsync

/**
 * @author hendrawd on 30/07/18
 */
class TeamViewModel : ViewModel() {
    private val team1LiveData: MutableLiveData<TeamModel> = MutableLiveData()
    private val team2LiveData: MutableLiveData<TeamModel> = MutableLiveData()

    fun loadTeam1(teamId: String) {
        doAsync {
            val jsonString = EndPoint.getTeam(teamId).getDataAsString
            val getTeamResponse = Gson().fromJson(jsonString, GetTeamListResponse::class.java)
            team1LiveData.postValue(getTeamResponse.teamList?.get(0)?.toTeamModel())
        }
    }

    fun loadTeam2(teamId: String) {
        doAsync {
            val jsonString = EndPoint.getTeam(teamId).getDataAsString
            val getTeamResponse = Gson().fromJson(jsonString, GetTeamListResponse::class.java)
            team2LiveData.postValue(getTeamResponse.teamList?.get(0)?.toTeamModel())
        }
    }

    fun getTeam1LiveData(): LiveData<TeamModel> {
        return team1LiveData
    }

    fun getTeam2LiveData(): LiveData<TeamModel> {
        return team2LiveData
    }
}

