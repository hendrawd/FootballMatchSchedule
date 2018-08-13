package ganteng.hendrawd.footballmatchschedule.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author hendrawd on 28/07/18
 */
@Parcelize
data class TeamStatisticsModel(
        val id: String,
        val name: String,
        val score: String,
        val goalDetails: String,
        val redCards: String,
        val yellowCards: String,
        val lineupGoalKeeper: String,
        val lineupDefense: String,
        val lineupMidfield: String,
        val lineupForward: String,
        val lineupSubstitutes: String,
        val formation: String
) : Parcelable