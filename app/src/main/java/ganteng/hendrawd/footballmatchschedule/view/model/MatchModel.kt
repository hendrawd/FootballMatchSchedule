package ganteng.hendrawd.footballmatchschedule.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MatchModel(
        val id: String,
        val team1Statistics: TeamStatisticsModel,
        val team2Statistics: TeamStatisticsModel,
        val date: String,
        val time: String
) : Parcelable