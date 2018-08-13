package ganteng.hendrawd.footballmatchschedule.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author hendrawd on 30/07/18
 */
@Parcelize
data class PlayerModel(
        val id: String,
        val avatarUrl: String,
        val backdropUrl: String,
        val name: String,
        val description: String,
        val weight: String,
        val height: String,
        val position: String
) : Parcelable