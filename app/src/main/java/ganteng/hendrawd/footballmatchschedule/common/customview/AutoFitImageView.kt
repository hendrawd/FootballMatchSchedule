package ganteng.hendrawd.footballmatchschedule.common.customview

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.View

/**
 * @author hendrawd on 11/21/16
 */
class AutoFitImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (drawable != null) {
            val w = View.MeasureSpec.getSize(widthMeasureSpec)
            val h = w * drawable.intrinsicHeight / drawable.intrinsicWidth
            setMeasuredDimension(w, h)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}