package ganteng.hendrawd.footballmatchschedule.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import ganteng.hendrawd.footballmatchschedule.R
import ganteng.hendrawd.footballmatchschedule.R.menu.activity_match_detail_menu
import ganteng.hendrawd.footballmatchschedule.db.FavoriteMatch
import ganteng.hendrawd.footballmatchschedule.common.util.Util
import ganteng.hendrawd.footballmatchschedule.common.util.load
import ganteng.hendrawd.footballmatchschedule.view.model.MatchModel
import ganteng.hendrawd.footballmatchschedule.viewmodel.MatchDetailViewModel
import ganteng.hendrawd.footballmatchschedule.viewmodel.TeamViewModel
import kotlinx.android.synthetic.main.activity_match_detail.*
import kotlinx.android.synthetic.main.layout_text_views_weight_three.view.*
import org.jetbrains.anko.textResource

/**
 * @author hendrawd on 28/07/18
 */
class MatchDetailActivity : BackButtonActivity() {

    private lateinit var matchDetailViewModel: MatchDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_detail)
        matchDetailViewModel = ViewModelProviders.of(this).get(MatchDetailViewModel::class.java)

        matchDetailViewModel.getMatchObservable().observe(this, Observer {
            it?.also {
                loadTeamLogos(it)
                showMainData(it)
                showGoalDetailsSection(it)
                showRedCardsSection(it)
                showYellowCardsSection(it)
                showLineupGoalKeeperSection(it)
                showLineupDefenseSection(it)
                showLineupMidfieldSection(it)
                showLineupForwardSection(it)
                showLineupSubstitutesSection(it)
                showFormationSection(it)
            }
        })

        matchDetailViewModel.setMatch(intent.getParcelableExtra("match"))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(activity_match_detail_menu, menu)
        matchDetailViewModel.getFavoriteMatchObservable().observe(this, Observer {
            menu.getItem(0)?.icon = ContextCompat.getDrawable(
                    this,
                    if (it is FavoriteMatch) {
                        R.drawable.ic_added_to_favorites
                    } else {
                        R.drawable.ic_add_to_favorites
                    }
            )
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_to_favorite -> {
                matchDetailViewModel.toggleFavoriteMatch()
                return true
            }
            R.id.add_to_calendar -> {
                val match = matchDetailViewModel.getMatchObservable().value
                match?.let {
                    addToCalendar(it)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addToCalendar(match: MatchModel) {
        val matchTitle = "${match.team1Statistics.name} VS ${match.team2Statistics.name}"
        val intent = Intent(Intent.ACTION_EDIT)
        intent.type = "vnd.android.cursor.item/event"
        intent.putExtra(CalendarContract.Events.TITLE, matchTitle)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, Util.toMillis(match.date, match.time))
                .putExtra(CalendarContract.Events.ALL_DAY, false)
        // .putExtra(CalendarContract.Events.DESCRIPTION, matchDesc)
        //intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endDateMillis);
        startActivity(intent)
    }

    override fun onBackPressed() {
        if (matchDetailViewModel.shouldRefreshFavoriteMatch) {
            setResult(RESULT_OK)
        }
        super.onBackPressed()
    }

    private fun showMainData(match: MatchModel) {
        tvDate.text = match.date
        tvTeam1Name.text = match.team1Statistics.name
        tvTeam2Name.text = match.team2Statistics.name
        tvTeam1Score.text = match.team1Statistics.score
        tvTeam2Score.text = match.team2Statistics.score
    }

    private fun loadTeamLogos(match: MatchModel) {
        val teamViewModel = ViewModelProviders.of(this).get(TeamViewModel::class.java)
        teamViewModel.getTeam1LiveData().observe(this, Observer {
            it?.also {
                ivTeam1Logo.load(it.logoUrl)
            }
        })
        teamViewModel.getTeam2LiveData().observe(this, Observer {
            it?.also {
                ivTeam2Logo.load(it.logoUrl)
            }
        })
        teamViewModel.loadTeam1(match.team1Statistics.id)
        teamViewModel.loadTeam2(match.team2Statistics.id)
    }

    private fun showGoalDetailsSection(match: MatchModel) {
        layout_group_goal_details.tvCenter.textResource = R.string.title_goal_details_section
        layout_group_goal_details.tvLeft.text = match.team1Statistics.goalDetails
        layout_group_goal_details.tvRight.text = match.team2Statistics.goalDetails
    }

    private fun showRedCardsSection(match: MatchModel) {
        layout_group_red_cards.tvCenter.textResource = R.string.title_red_cards
        layout_group_red_cards.tvLeft.text = match.team1Statistics.redCards
        layout_group_red_cards.tvRight.text = match.team2Statistics.redCards
    }

    private fun showYellowCardsSection(match: MatchModel) {
        layout_group_yellow_cards.tvCenter.textResource = R.string.title_yellow_cards
        layout_group_yellow_cards.tvLeft.text = match.team1Statistics.yellowCards
        layout_group_yellow_cards.tvRight.text = match.team2Statistics.yellowCards
    }

    private fun showLineupGoalKeeperSection(match: MatchModel) {
        layout_group_lineup_goal_keeper.tvCenter.textResource = R.string.title_lineup_goal_keeper
        layout_group_lineup_goal_keeper.tvLeft.text = match.team1Statistics.lineupGoalKeeper
        layout_group_lineup_goal_keeper.tvRight.text = match.team2Statistics.lineupGoalKeeper
    }

    private fun showLineupDefenseSection(match: MatchModel) {
        layout_group_lineup_defense.tvCenter.textResource = R.string.title_lineup_defense
        layout_group_lineup_defense.tvLeft.text = match.team1Statistics.lineupDefense
        layout_group_lineup_defense.tvRight.text = match.team2Statistics.lineupDefense
    }

    private fun showLineupMidfieldSection(match: MatchModel) {
        layout_group_lineup_mid_field.tvCenter.textResource = R.string.title_lineup_mid_field
        layout_group_lineup_mid_field.tvLeft.text = match.team1Statistics.lineupMidfield
        layout_group_lineup_mid_field.tvRight.text = match.team2Statistics.lineupMidfield
    }

    private fun showLineupForwardSection(match: MatchModel) {
        layout_group_lineup_forward.tvCenter.textResource = R.string.title_lineup_forward
        layout_group_lineup_forward.tvLeft.text = match.team1Statistics.lineupForward
        layout_group_lineup_forward.tvRight.text = match.team2Statistics.lineupForward
    }

    private fun showLineupSubstitutesSection(match: MatchModel) {
        layout_group_lineup_substitutes.tvCenter.textResource = R.string.title_lineup_substitutes
        layout_group_lineup_substitutes.tvLeft.text = match.team1Statistics.lineupSubstitutes
        layout_group_lineup_substitutes.tvRight.text = match.team2Statistics.lineupSubstitutes
    }

    private fun showFormationSection(match: MatchModel) {
        layout_group_formation.tvCenter.textResource = R.string.title_formation
        layout_group_formation.tvLeft.text = match.team1Statistics.formation
        layout_group_formation.tvRight.text = match.team2Statistics.formation
    }
}