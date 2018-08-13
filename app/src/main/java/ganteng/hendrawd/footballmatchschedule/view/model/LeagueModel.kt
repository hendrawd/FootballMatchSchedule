package ganteng.hendrawd.footballmatchschedule.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author hendrawd on 06/08/18
 */
@Parcelize
data class LeagueModel(
        val id: String,
        val name: String
) : Parcelable {
    override fun toString(): String {
        return name
    }
}