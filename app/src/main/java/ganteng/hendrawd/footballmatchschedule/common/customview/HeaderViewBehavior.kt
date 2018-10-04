package ganteng.hendrawd.footballmatchschedule.common.customview

import android.content.Context
import android.os.Build
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

import ganteng.hendrawd.footballmatchschedule.R

class HeaderViewBehavior(private val mContext: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<HeaderView>() {
    private var mStartMarginLeft: Int = 0
    private var mEndMarginLeft: Int = 0
    private var mMarginRight: Int = 0
    private var mStartMarginBottom: Int = 0
    private var isHide: Boolean = false

    private val toolbarHeight: Int
        get() {
            var result = 0
            val tv = TypedValue()
            if (mContext.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                result = TypedValue.complexToDimensionPixelSize(tv.data, mContext.resources.displayMetrics)
            }
            return result
        }

    override fun layoutDependsOn(parent: CoordinatorLayout, child: HeaderView, dependency: View): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: HeaderView, dependency: View): Boolean {
        initProperties()

        val maxScroll = (dependency as AppBarLayout).totalScrollRange
        val percentage = Math.abs(dependency.y) / maxScroll.toFloat()

        var childPosition = (dependency.height + dependency.y
                - child.height.toFloat()
                - (toolbarHeight - child.height) * percentage / 2)


        childPosition -= mStartMarginBottom * (1f - percentage)

        val lp = child.layoutParams as CoordinatorLayout.LayoutParams
        lp.leftMargin = (percentage * mEndMarginLeft).toInt() + mStartMarginLeft
        lp.rightMargin = mMarginRight
        child.layoutParams = lp

        child.y = childPosition

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (isHide && percentage < 1) {
                child.visibility = View.VISIBLE
                isHide = false
            } else if (!isHide && percentage == 1f) {
                child.visibility = View.GONE
                isHide = true
            }
        }
        return true
    }

    private fun initProperties() {
        if (mStartMarginLeft == 0)
            mStartMarginLeft = mContext.resources.getDimensionPixelOffset(R.dimen.header_view_start_margin_left)

        if (mEndMarginLeft == 0)
            mEndMarginLeft = mContext.resources.getDimensionPixelOffset(R.dimen.header_view_end_margin_left)

        if (mStartMarginBottom == 0)
            mStartMarginBottom = mContext.resources.getDimensionPixelOffset(R.dimen.header_view_start_margin_bottom)

        if (mMarginRight == 0)
            mMarginRight = mContext.resources.getDimensionPixelOffset(R.dimen.header_view_end_margin_right)

    }
}