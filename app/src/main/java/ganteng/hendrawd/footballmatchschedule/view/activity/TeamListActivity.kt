package ganteng.hendrawd.footballmatchschedule.view.activity

import android.app.Activity
import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import com.roger.catloadinglibrary.CatLoadingView
import ganteng.hendrawd.footballmatchschedule.R
import ganteng.hendrawd.footballmatchschedule.common.util.Loading
import ganteng.hendrawd.footballmatchschedule.view.adapter.TeamAdapter
import ganteng.hendrawd.footballmatchschedule.view.model.LeagueModel
import ganteng.hendrawd.footballmatchschedule.viewmodel.TeamListViewModel
import kotlinx.android.synthetic.main.activity_list.*
import org.jetbrains.anko.intentFor


class TeamListActivity : BackButtonActivity(), Loading {

    companion object {
        const val RC_CHECK_FAVORITE = 12
        const val KEY_LEAGUE = "KEY_LEAGUE"
    }

    private var catLoadingView: CatLoadingView? = null
    private lateinit var teamListViewModel: TeamListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setRecyclerViewState()
        val league = intent.getParcelableExtra<LeagueModel>(KEY_LEAGUE)
        teamListViewModel = ViewModelProviders.of(this).get(TeamListViewModel::class.java)
        teamListViewModel.setLeague(league)
        title = league.name
        navigation.inflateMenu(R.menu.menu_navigation_team)

        observeGetTeams()
        setNavigationListener()
        // load first data
        loadTeamsWithProgressDialog()

        swipe_refresh.setRecyclerView(rv)
        swipe_refresh.setOnRefreshListener { refreshTeams() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_CHECK_FAVORITE && resultCode == Activity.RESULT_OK) {
            if (navigation.selectedItemId == R.id.menu_favorites) {
                loadFavoriteTeamsWithProgressDialog()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchManager = this@TeamListActivity.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        var searchView: SearchView? = null
        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(this@TeamListActivity.componentName))
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // submit search
//                if (!searchView.isIconified) {
//                    searchView.isIconified = true
//                }
//                searchItem.collapseActionView()
//                searchMatches(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                // live search
                searchMatches(query)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun refreshTeams() {
        when (navigation.selectedItemId) {
            R.id.menu_teams -> teamListViewModel.loadTeams(refresh = true)
            R.id.menu_favorites -> teamListViewModel.loadFavoriteTeams()
        }
    }

    private fun searchMatches(query: String? = null) {
        when (navigation.selectedItemId) {
            R.id.menu_teams -> teamListViewModel.loadTeams(query)
            R.id.menu_favorites -> teamListViewModel.loadFavoriteTeams(query)
        }
    }

    private fun observeGetTeams() {
        teamListViewModel.getTeamsObservable().observe(this, Observer {
            it?.let {
                if (it.isEmpty()) {
                    rv.visibility = View.GONE
                    iv_no_data.visibility = View.VISIBLE
                } else {
                    val teamAdapter = TeamAdapter(it)
                    observeTeamAdapterClick(teamAdapter)
                    rv.adapter = teamAdapter
                    rv.visibility = View.VISIBLE
                    iv_no_data.visibility = View.GONE
                }
                dismissLoading()
            }
        })
    }

    private fun observeTeamAdapterClick(teamAdapter: TeamAdapter) {
        teamAdapter.clickSubject.observe(this, Observer {
            it?.let {
                val intent = intentFor<TeamDetailActivity>("team" to it["teamModel"])
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val imageView = it["imageView"]
                    val textView = it["textView"]
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            this,
                            Pair.create(imageView as View, imageView.transitionName),
                            Pair.create(textView as View, textView.transitionName)
                    ).toBundle()
                    intent.putExtra("imageTransition", imageView.transitionName)
                    intent.putExtra("nameTransition", textView.transitionName)
                    ActivityCompat.startActivityForResult(
                            this,
                            intent,
                            RC_CHECK_FAVORITE,
                            options
                    )
                } else {
                    startActivityForResult(intent, RC_CHECK_FAVORITE)
                }
            }
        })

    }

    private fun setNavigationListener() {
        navigation.setOnNavigationItemSelectedListener {
            title = when (it.itemId) {
                R.id.menu_teams -> {
                    loadTeamsWithProgressDialog()
                    teamListViewModel.getLeague().name
                }
                else -> {
                    loadFavoriteTeamsWithProgressDialog()
                    getString(R.string.favorite_teams)
                }
            }
            true
        }
    }

    private fun setRecyclerViewState() {
        rv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        rv.layoutManager = LinearLayoutManager(this@TeamListActivity)
    }

    private fun loadTeamsWithProgressDialog() {
        teamListViewModel.loadTeams()
        showLoading()
    }

    private fun loadFavoriteTeamsWithProgressDialog() {
        teamListViewModel.loadFavoriteTeams()
        showLoading()
    }

    override fun showLoading() {
        if (catLoadingView == null) {
            catLoadingView = CatLoadingView()
            catLoadingView?.isCancelable = false
        }
        catLoadingView?.show(supportFragmentManager, "")
    }

    override fun dismissLoading() {
        catLoadingView?.dismissAllowingStateLoss()
        swipe_refresh.isRefreshing = false
    }
}
