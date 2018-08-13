package ganteng.hendrawd.footballmatchschedule.view.custom

import android.content.Context
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

    private var paint: Paint? = null
    private var path: Path? = null
    private var lineColor = Color.BLACK

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, `as`: AttributeSet) : super(context, `as`) {

        val a = context.obtainStyledAttributes(`as`, R.styleable.ContinuedLineView)

        val color = a.getString(R.styleable.ContinuedLineView_android_color)
        lineColor = Color.parseColor(color)

        a.recycle()

        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {

        val a = context.obtainStyledAttributes(attrs, R.styleable.ContinuedLineView, defStyle, 0)

        val color = a.getString(R.styleable.ContinuedLineView_android_color)
        lineColor = Color.parseColor(color)

        a.recycle()

        init(context)
    }

    private fun init(context: Context) {
        paint = Paint()
        paint!!.color = lineColor
        paint!!.isAntiAlias = true
        paint!!.style = Paint.Style.STROKE
        paint!!.strokeJoin = Paint.Join.ROUND
        paint!!.strokeCap = Paint.Cap.ROUND
        path = Path()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
    }

    override fun onDraw(canvas: Canvas) {
        paint!!.strokeWidth = height.toFloat()
        path!!.reset()
        path!!.moveTo(height.toFloat(), (height / 2).toFloat())
        path!!.lineTo((width - height).toFloat(), (height / 2).toFloat())
        canvas.drawPath(path!!, paint!!)
    }
}
