package ganteng.hendrawd.footballmatchschedule.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.DividerItemDecoration.VERTICAL
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.roger.catloadinglibrary.CatLoadingView
import ganteng.hendrawd.footballmatchschedule.common.util.Loading
import ganteng.hendrawd.footballmatchschedule.common.util.getActivity
import ganteng.hendrawd.footballmatchschedule.view.activity.PlayerDetailActivity
import ganteng.hendrawd.footballmatchschedule.view.adapter.PlayerAdapter
import ganteng.hendrawd.footballmatchschedule.viewmodel.PlayerListViewModel
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.intentFor


/**
 * @author hendrawd on 08/08/18
 */
class PlayerListFragment : Fragment(), Loading {

    companion object {
        fun newInstance(teamId: String): PlayerListFragment {
            val args = Bundle()
            args.putString("teamId", teamId)
            val playerListFragment = PlayerListFragment()
            playerListFragment.arguments = args
            return playerListFragment
        }
    }

    lateinit var playerListViewModel: PlayerListViewModel
    private var catLoadingView: CatLoadingView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerListViewModel = ViewModelProviders.of(this).get(PlayerListViewModel::class.java)
        val teamId = arguments?.getString("teamId") ?: ""
        playerListViewModel.setTeamId(teamId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        showLoading()
        lateinit var playerListRecyclerView: RecyclerView
        val rootView = UI {
            playerListRecyclerView = recyclerView {
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(DividerItemDecoration(context, VERTICAL))
            }
        }.view
        playerListViewModel.getPlayerListObservable().observe(this, Observer {
            it?.let {
                val playerAdapter = PlayerAdapter(it)
                playerAdapter.clickSubject.observe(this, Observer {
                    it?.let {
                        listenToPlayerItemClick(it)
                    }
                })
                playerListRecyclerView.adapter = playerAdapter
                dismissLoading()
            }
        })
        playerListViewModel.loadPlayerList()
        return rootView
    }

    private fun listenToPlayerItemClick(clickData: Map<String, *>) {
        val parentActivity = context?.getActivity()
        if (parentActivity != null) {
            val intent = intentFor<PlayerDetailActivity>("player" to clickData["playerModel"])
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val tvPlayerName = clickData["textViewName"]
                val tvPlayerPosition = clickData["textViewPosition"]
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        parentActivity,
                        Pair.create(tvPlayerName as View, tvPlayerName.transitionName),
                        Pair.create(tvPlayerPosition as View, tvPlayerPosition.transitionName)
                ).toBundle()
                intent.putExtra("nameTransition", tvPlayerName.transitionName)
                intent.putExtra("positionTransition", tvPlayerPosition.transitionName)
                ActivityCompat.startActivity(
                        parentActivity,
                        intent,
                        options
                )
            } else {
                startActivity(intent)
            }
        }
    }

    override fun showLoading() {
        if (catLoadingView == null) {
            catLoadingView = CatLoadingView()
            catLoadingView?.isCancelable = false
        }
        val activity = context?.getActivity()
        activity?.let {
            if (it is AppCompatActivity) {
                catLoadingView?.show(it.supportFragmentManager, "")
            }
        }
    }

    override fun dismissLoading() {
        catLoadingView?.dismissAllowingStateLoss()
    }
}