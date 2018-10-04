package ganteng.hendrawd.footballmatchschedule.view.adapter

import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ganteng.hendrawd.footballmatchschedule.R
import ganteng.hendrawd.footballmatchschedule.common.util.SingleLiveEvent
import ganteng.hendrawd.footballmatchschedule.common.util.load
import ganteng.hendrawd.footballmatchschedule.view.model.PlayerModel
import kotlinx.android.synthetic.main.item_list_player.view.*

/**
 * @author hendrawd on 22/07/18
 */
class PlayerAdapter(private val playerList: List<PlayerModel>) : RecyclerView.Adapter<PlayerAdapter.ViewHolder>() {

    val clickSubject by lazy { SingleLiveEvent<Map<String, *>>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(
                R.layout.item_list_player,
                parent,
                false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playerList.size
    }

    override fun onBindViewHolder(holder: PlayerAdapter.ViewHolder, position: Int) {
        val player = playerList[position]
        holder.bind(player)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                val data = mapOf(
                        "textViewName" to it.tvPlayerName,
                        "textViewPosition" to it.tvPlayerPosition,
                        "playerModel" to playerList[adapterPosition]
                )
                clickSubject.postValue(data)
            }
        }

        fun bind(player: PlayerModel) {
            with(itemView) {
                tvPlayerName.text = player.name
                tvPlayerPosition.text = player.position
                ivPlayerAvatar.load(player.avatarUrl)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tvPlayerName.transitionName = "name${player.id}"
                    tvPlayerPosition.transitionName = "position${player.id}"
                    ivPlayerAvatar.transitionName = "image${player.id}"
                }
            }
        }
    }
}