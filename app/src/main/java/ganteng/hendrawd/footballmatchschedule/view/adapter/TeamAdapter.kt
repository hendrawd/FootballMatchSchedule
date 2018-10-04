package ganteng.hendrawd.footballmatchschedule.view.adapter

import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ganteng.hendrawd.footballmatchschedule.R
import ganteng.hendrawd.footballmatchschedule.common.util.SingleLiveEvent
import ganteng.hendrawd.footballmatchschedule.common.util.load
import ganteng.hendrawd.footballmatchschedule.view.model.TeamModel
import kotlinx.android.synthetic.main.item_list_team.view.*

/**
 * @author hendrawd on 22/07/18
 */
class TeamAdapter(private val teamList: List<TeamModel>) : RecyclerView.Adapter<TeamAdapter.ViewHolder>() {

    val clickSubject by lazy { SingleLiveEvent<Map<String, *>>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(
                R.layout.item_list_team,
                parent,
                false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return teamList.size
    }

    override fun onBindViewHolder(holder: TeamAdapter.ViewHolder, position: Int) {
        val match = teamList[position]
        holder.bind(match)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                val data = mapOf(
                        "imageView" to it.ivTeamLogo,
                        "textView" to it.tvTeamName,
                        "teamModel" to teamList[adapterPosition]
                )
                clickSubject.postValue(data)
            }
        }

        fun bind(team: TeamModel) {
            with(itemView) {
                tvTeamName.text = team.name
                ivTeamLogo.load(team.logoUrl)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tvTeamName.transitionName = "name${team.id}"
                    ivTeamLogo.transitionName = "image${team.id}"
                }
            }
        }
    }
}