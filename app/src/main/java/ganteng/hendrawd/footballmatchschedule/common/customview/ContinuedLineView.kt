package ganteng.hendrawd.footballmatchschedule.common.customview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import ganteng.hendrawd.footballmatchschedule.R

/**
 * Draw line is buggy for layertype hardware and lollipop+
 * and divider inconsistency of listview among android versions
 * need to replaced with this method instead to use default divider
 * @author hendrawd on 11/4/15
 */
class ContinuedLineView : View {
    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }
    private val path = Path()
    private var lineColor = Color.BLACK

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        obtainColor(context.obtainStyledAttributes(attrs, R.styleable.ContinuedLineView))
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        obtainColor(context.obtainStyledAttributes(attrs, R.styleable.ContinuedLineView, defStyle, 0))
    }

    private fun obtainColor(typedArray: TypedArray) {
        typedArray.apply {
            paint.color = getColor(R.styleable.ContinuedLineView_android_color, lineColor)
            recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        paint.strokeWidth = height.toFloat()
        path.apply {
            reset()
            val halfHeight = (height / 2).toFloat()
            moveTo(height.toFloat(), halfHeight)
            lineTo((width - height).toFloat(), halfHeight)
            canvas.drawPath(this, paint)
        }
    }
}
