package ganteng.hendrawd.footballmatchschedule.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import android.view.ViewTreeObserver
import ganteng.hendrawd.footballmatchschedule.R
import ganteng.hendrawd.footballmatchschedule.db.FavoriteTeam
import ganteng.hendrawd.footballmatchschedule.util.load
import ganteng.hendrawd.footballmatchschedule.view.fragment.PlayerListFragment
import ganteng.hendrawd.footballmatchschedule.view.fragment.TeamDescriptionFragment
import ganteng.hendrawd.footballmatchschedule.viewmodel.TeamDetailViewModel
import kotlinx.android.synthetic.main.activity_team_detail.*


/**
 * @author hendrawd on 08/08/18
 */
class TeamDetailActivity : BackButtonActivity() {

    private lateinit var teamDetailViewModel: TeamDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_team_detail)
        setSupportActionBar(toolbar)
        super.onCreate(savedInstanceState)
        teamDetailViewModel = ViewModelProviders.of(this).get(TeamDetailViewModel::class.java)
        teamDetailViewModel.setTeam(intent.getParcelableExtra("team"))
        tryToUseSharedElementTransition()
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_team_detail_menu, menu)
        teamDetailViewModel.getFavoriteTeamObservable().observe(this, Observer {
            menu.getItem(0)?.icon = ContextCompat.getDrawable(
                    this,
                    if (it is FavoriteTeam) {
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
                teamDetailViewModel.toggleFavoriteMatch()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (teamDetailViewModel.shouldRefreshFavoriteTeam) {
            setResult(RESULT_OK)
        }
        super.onBackPressed()
    }

    private fun init() {
        val teamModel = teamDetailViewModel.getTeam()!!
        collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, R.color.colorAccent))
        collapsingToolbar.setContentScrimColor(Color.parseColor("#aa000000"))
        appbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = true
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.title = teamModel.name
                    isShow = true
                } else if (isShow) {
                    collapsingToolbar.title = " "
                    isShow = false
                }
            }
        })
        ivTeamLogo.load(teamModel.logoUrl)
        tvTeamName.text = teamModel.name
        tvFormedYear.text = teamModel.formedYear
        tvStadiumLocation.text = teamModel.stadium

        // setup view pager and adapter
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(createTeamDescriptionFragment(), "Description")
        adapter.addFragment(createPlayerListFragment(teamModel.id), "Player List")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun tryToUseSharedElementTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val imageTransitionName = intent.getStringExtra("imageTransition")
            val nameTransitionName = intent.getStringExtra("nameTransition")
            ivTeamLogo.transitionName = imageTransitionName
            tvTeamName.transitionName = nameTransitionName
            supportPostponeEnterTransition()
            ivTeamLogo.viewTreeObserver.addOnPreDrawListener(
                    object : ViewTreeObserver.OnPreDrawListener {
                        override fun onPreDraw(): Boolean {
                            ivTeamLogo.viewTreeObserver.removeOnPreDrawListener(this)
                            supportStartPostponedEnterTransition()
                            return true
                        }
                    }
            )
        }
    }

    private fun createTeamDescriptionFragment(): Fragment {
        val description = teamDetailViewModel.getTeam()?.description ?: ""
        return TeamDescriptionFragment.newInstance(description)
    }

    private fun createPlayerListFragment(teamId: String): Fragment {
        return PlayerListFragment.newInstance(teamId)
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList = mutableListOf<Fragment>()
        private val mFragmentTitleList = mutableListOf<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }
    }
}