package ganteng.hendrawd.footballmatchschedule.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author hendrawd on 30/07/18
 */
@Parcelize
data class TeamModel(
        val id: String,
        val logoUrl: String,
        val backdropUrl: String,
        val name: String,
        val description: String,
        val formedYear: String,
        val stadium: String
) : Parcelable