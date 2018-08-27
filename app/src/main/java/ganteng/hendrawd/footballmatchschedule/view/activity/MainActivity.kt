package ganteng.hendrawd.footballmatchschedule.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.roger.catloadinglibrary.CatLoadingView
import ganteng.hendrawd.footballmatchschedule.R
import ganteng.hendrawd.footballmatchschedule.util.Loading
import ganteng.hendrawd.footballmatchschedule.view.activity.MatchListActivity.Companion.KEY_LEAGUE
import ganteng.hendrawd.footballmatchschedule.view.model.LeagueModel
import ganteng.hendrawd.footballmatchschedule.viewmodel.LeagueListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.longToast

/**
 * @author hendrawd on 06/08/18
 */
class MainActivity : AppCompatActivity(), Loading {

    private var catLoadingView: CatLoadingView? = null
    private lateinit var leagueListViewModel: LeagueListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        leagueListViewModel = ViewModelProviders.of(this).get(LeagueListViewModel::class.java)
        leagueListViewModel.getLeaguesObservable().observe(this, Observer {
            it?.let {
                dismissLoading()
                val adapter = ArrayAdapter<LeagueModel>(this, android.R.layout.simple_spinner_item)
                adapter.addAll(it)
                spinner.adapter = adapter
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        leagueListViewModel.setSelectedLeague(it[position])
                    }
                }
            }
        })

        bLeague.setOnClickListener {
            val selectedLeague = leagueListViewModel.getSelectedLeague()
            if (selectedLeague == null) {
                longToast("Please select league first")
            } else {
                startActivity(intentFor<MatchListActivity>(
                        KEY_LEAGUE to selectedLeague
                ))
            }
        }
        bTeam.setOnClickListener {
            val selectedLeague = leagueListViewModel.getSelectedLeague()
            if (selectedLeague == null) {
                longToast("Please select league first")
            } else {
                startActivity(intentFor<TeamListActivity>(
                        KEY_LEAGUE to selectedLeague
                ))
            }
        }

        showLoading()
        leagueListViewModel.loadLeagues()
    }

    override fun showLoading() {
        if (catLoadingView == null) {
            catLoadingView = CatLoadingView()
            catLoadingView?.isCancelable = false
        }
        catLoadingView?.show(supportFragmentManager, "")
    }

    override fun dismissLoading() {
        catLoadingView?.dismiss()
    }
}