package ganteng.hendrawd.footballmatchschedule.view.custom

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.layout_toolbar_header_view.view.*

class HeaderView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    @JvmOverloads
    fun bindTo(title: String, subTitle: String = "") {
        hideOrSetText(header_view_title, title)
        hideOrSetText(header_view_sub_title, subTitle)
    }

    private fun hideOrSetText(tv: TextView, text: String?) {
        if (text == null || text == "") {
            tv.visibility = View.GONE
        } else {
            tv.visibility = View.VISIBLE
            tv.text = text
        }
    }
}