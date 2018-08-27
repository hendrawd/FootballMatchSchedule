package ganteng.hendrawd.footballmatchschedule.view.activity

import android.app.Activity
import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import com.roger.catloadinglibrary.CatLoadingView
import ganteng.hendrawd.footballmatchschedule.R
import ganteng.hendrawd.footballmatchschedule.R.menu.menu_navigation_match
import ganteng.hendrawd.footballmatchschedule.util.Loading
import ganteng.hendrawd.footballmatchschedule.view.adapter.MatchAdapter
import ganteng.hendrawd.footballmatchschedule.view.model.LeagueModel
import ganteng.hendrawd.footballmatchschedule.viewmodel.MatchListViewModel
import kotlinx.android.synthetic.main.activity_list.*
import org.jetbrains.anko.intentFor


class MatchListActivity : BackButtonActivity(), Loading {

    companion object {
        const val RC_CHECK_FAVORITE = 12
        const val KEY_LEAGUE = "KEY_LEAGUE"
    }

    private var catLoadingView: CatLoadingView? = null
    private lateinit var matchListViewModel: MatchListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setRecyclerViewState()
        val league = intent.getParcelableExtra<LeagueModel>(KEY_LEAGUE)
        matchListViewModel = ViewModelProviders.of(this).get(MatchListViewModel::class.java)
        matchListViewModel.setLeague(league)
        title = league.name
        navigation.inflateMenu(menu_navigation_match)

        observeGetMatches()
        setNavigationListener()
        // load first data
        loadPreviousMatchesWithProgressDialog()

        swipe_refresh.setRecyclerView(rv)
        swipe_refresh.setOnRefreshListener { refreshMatches() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_CHECK_FAVORITE && resultCode == Activity.RESULT_OK) {
            if (navigation.selectedItemId == R.id.menu_favorites) {
                loadFavoriteMatchesWithProgressDialog()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchManager = this@MatchListActivity.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        var searchView: SearchView? = null
        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(this@MatchListActivity.componentName))
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

    private fun refreshMatches() {
        when (navigation.selectedItemId) {
            R.id.menu_next -> matchListViewModel.loadNextMatches(refresh = true)
            R.id.menu_prev -> matchListViewModel.loadPreviousMatches(refresh = true)
            R.id.menu_favorites -> matchListViewModel.loadFavoriteMatches()
        }
    }

    private fun searchMatches(query: String? = null) {
        when (navigation.selectedItemId) {
            R.id.menu_next -> matchListViewModel.loadNextMatches(query)
            R.id.menu_prev -> matchListViewModel.loadPreviousMatches(query)
            R.id.menu_favorites -> matchListViewModel.loadFavoriteMatches(query)
        }
    }

    private fun observeGetMatches() {
        matchListViewModel.getMatchesObservable().observe(this, Observer {
            it?.let {
                if (it.isEmpty()) {
                    rv.visibility = View.GONE
                    iv_no_data.visibility = View.VISIBLE
                } else {
                    val matchAdapter = MatchAdapter(it)
                    observeMatchAdapterClick(matchAdapter)
                    rv.adapter = matchAdapter
                    rv.visibility = View.VISIBLE
                    iv_no_data.visibility = View.GONE
                }
                dismissLoading()
            }
        })
    }

    private fun observeMatchAdapterClick(matchAdapter: MatchAdapter) {
        matchAdapter.clickSubject.observe(this, Observer {
            it?.let {
                val intent = intentFor<MatchDetailActivity>("match" to it)
                startActivityForResult(intent, RC_CHECK_FAVORITE)
            }
        })

    }

    private fun setNavigationListener() {
        navigation.setOnNavigationItemSelectedListener {
            title = when (it.itemId) {
                R.id.menu_prev -> {
                    loadPreviousMatchesWithProgressDialog()
                    matchListViewModel.getLeague().name
                }
                R.id.menu_next -> {
                    loadNextMatchesWithProgressDialog()
                    matchListViewModel.getLeague().name
                }
                else -> {
                    loadFavoriteMatchesWithProgressDialog()
                    getString(R.string.favorite_matches)
                }
            }
            true
        }
    }

    private fun setRecyclerViewState() {
        rv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        rv.layoutManager = LinearLayoutManager(this@MatchListActivity)
    }

    private fun loadPreviousMatchesWithProgressDialog() {
        matchListViewModel.loadPreviousMatches()
        showLoading()
    }

    private fun loadNextMatchesWithProgressDialog() {
        matchListViewModel.loadNextMatches()
        showLoading()
    }

    private fun loadFavoriteMatchesWithProgressDialog() {
        matchListViewModel.loadFavoriteMatches()
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
        catLoadingView?.dismiss()
        swipe_refresh.isRefreshing = false
    }
}
