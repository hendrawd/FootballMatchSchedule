package ganteng.hendrawd.footballmatchschedule.view.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.view.View
import ganteng.hendrawd.footballmatchschedule.R
import ganteng.hendrawd.footballmatchschedule.util.load
import ganteng.hendrawd.footballmatchschedule.view.custom.HeaderView
import ganteng.hendrawd.footballmatchschedule.view.model.PlayerModel
import kotlinx.android.synthetic.main.activity_player_detail.*
import kotlinx.android.synthetic.main.layout_toolbar_header_view.view.*

/**
 * @author hendrawd on 09/08/18
 */
class PlayerDetailActivity : BackButtonActivity(), AppBarLayout.OnOffsetChangedListener {

    private var isHideToolbarView = false

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        val maxScroll = appBarLayout.totalScrollRange
        val percentage = Math.abs(verticalOffset).toFloat() / maxScroll.toFloat()

        if (percentage == 1f && isHideToolbarView) {
            toolbarHeaderView.visibility = View.VISIBLE
            isHideToolbarView = !isHideToolbarView

        } else if (percentage < 1f && !isHideToolbarView) {
            toolbarHeaderView.visibility = View.GONE
            isHideToolbarView = !isHideToolbarView
        }
    }

    private fun bindToolbar(title: String, subTitle: String) {
        (toolbarHeaderView as HeaderView).bindTo(title, subTitle)
        (floatHeaderView as HeaderView).bindTo(title, subTitle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_player_detail)
        setSupportActionBar(toolbarPlayerDetail)
        super.onCreate(savedInstanceState)

        val playerModel = intent.getParcelableExtra<PlayerModel>("player")
        tvPosition.text = playerModel.position
        tvWeight.text = playerModel.weight
        tvHeight.text = playerModel.height
        tvDescription.text = playerModel.description
        ivBackdropImagePlayerDetail.load(playerModel.backdropUrl)

        //remove all titles, because using custom header view
        collapsingToolbarPlayerDetail.title = ""
        collapsingToolbarPlayerDetail.isTitleEnabled = false
        supportActionBar?.title = ""

        collapsingToolbarPlayerDetail.setContentScrimColor(Color.parseColor("#00000000"))//transparent

        bindToolbar(playerModel.name, "")

        appbarPlayerDetail.addOnOffsetChangedListener(this)

        tryToUseSharedElementTransition()
    }

    private fun tryToUseSharedElementTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val nameTransitionName = intent.getStringExtra("nameTransition")
            val positionTransitionName = intent.getStringExtra("positionTransition")
            floatHeaderView.header_view_title.transitionName = nameTransitionName
            tvPosition.transitionName = positionTransitionName
        }
    }
}