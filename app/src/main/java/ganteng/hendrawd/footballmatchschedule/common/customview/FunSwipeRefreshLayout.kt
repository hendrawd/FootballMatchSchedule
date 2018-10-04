package ganteng.hendrawd.footballmatchschedule.common.customview

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

/**
 * SwipeRefresh is not working after setting an empty view for listview which is the only child of a SwipeRefresh layout
 * refer to http://stackoverflow.com/questions/22959118/android-support-v4-swiperefreshlayout-empty-view-issue/32357955#32357955
 * @author hendrawd on 5/18/16
 */
class FunSwipeRefreshLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs) {
    init {
        setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light)
    }
    private var mRecyclerView: RecyclerView? = null

    fun setRecyclerView(recyclerView: RecyclerView) {
        mRecyclerView = recyclerView
    }

    override fun canChildScrollUp(): Boolean {
        return mRecyclerView == null || mRecyclerView!!.canScrollVertically(-1)
    }
}