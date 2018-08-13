package ganteng.hendrawd.footballmatchschedule.view.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ganteng.hendrawd.footballmatchschedule.R
import ganteng.hendrawd.footballmatchschedule.util.SingleLiveEvent
import ganteng.hendrawd.footballmatchschedule.view.model.MatchModel
import kotlinx.android.synthetic.main.item_list_match.view.*

/**
 * @author hendrawd on 22/07/18
 */
class MatchAdapter(private val matchList: List<MatchModel>) : RecyclerView.Adapter<MatchAdapter.ViewHolder>() {

    val clickSubject by lazy { SingleLiveEvent<MatchModel>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(
                R.layout.item_list_match,
                parent,
                false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return matchList.size
    }

    override fun onBindViewHolder(holder: MatchAdapter.ViewHolder, position: Int) {
        val match = matchList[position]
        holder.bind(match)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                clickSubject.postValue(matchList[adapterPosition])
            }
        }

        fun bind(match: MatchModel) {
            with(itemView) {
                tvDate.text = match.date
                tvTeam1Name.text = match.team1Statistics.name
                tvTeam2Name.text = match.team2Statistics.name
                tvTeam1Score.text = match.team1Statistics.score
                tvTeam2Score.text = match.team2Statistics.score
            }
        }
    }
}