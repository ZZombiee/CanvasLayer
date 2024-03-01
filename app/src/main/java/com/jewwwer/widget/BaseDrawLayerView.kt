package com.jewwwer.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.kyle.common.kt.extend.dp
import com.kyle.common.kt.extend.initConfig
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * desc:
 * @author:Jewwwer
 * @Date:2024/3/1
 */
abstract class BaseDrawLayerView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    val mBuild = CanvasLayer.Builder().apply {
        setBuildBridge(object : CanvasLayer.Builder.BuildBridge {
            override fun getViewWidth() = measuredWidth.toFloat()
            override fun getViewHeight() = measuredHeight.toFloat()
        })
    }
    private val mPaint = Paint().initConfig()
    private val mLayerHeight: Float

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.BaseDrawLayerView)
        mLayerHeight = ta.getDimension(R.styleable.BaseDrawLayerView_layer_height, -1F)
        ta.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        refreshLayerRect(true)
    }

    fun refreshLayerRect(onMeasure: Boolean = false) {
        if (measuredHeight != 0) {
            getLayerBuilder().invoke(mBuild)
            val rect = mBuild.getLayerRect()
            if (measuredWidth == (rect[2].toInt() + paddingEnd + paddingStart) && measuredHeight == (if (mLayerHeight != -1F) mLayerHeight.toInt() else rect[3].toInt())) return
            if (rect.isNotEmpty()) {
                if (onMeasure)
                    setMeasuredDimension(rect[2].toInt() + paddingEnd + paddingStart, if (mLayerHeight != -1F) mLayerHeight.toInt() else rect[3].toInt())
                else {
                    val params = layoutParams
                    params.width = rect[2].toInt() + paddingEnd + paddingStart
                    params.height = if (mLayerHeight != -1F) mLayerHeight.toInt() else rect[3].toInt()
                    layoutParams = params
                }
            }
        }
    }

    private var mDownX = 0F
    private var mDownY = 0F
    val maximumEffectiveDistance = 5.dp
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        var consume = false
        if (event?.action == MotionEvent.ACTION_DOWN) {
            mDownX = event.x
            mDownY = event.y
        } else if (event?.action == MotionEvent.ACTION_UP) {
            val diffX = abs(mDownX - event.x)
            val diffY = abs(mDownY - event.y)
            if (sqrt(diffX.toDouble().pow(2) + diffY.toDouble().pow(2)) <= maximumEffectiveDistance) {
                val call = mBuild.findClickItem(event.x, event.y)
                call?.invoke()
                consume = call != null
            }
        }
        return if (consume) true else super.dispatchTouchEvent(event)
    }

    abstract fun getLayerBuilder(): CanvasLayer.Builder.() -> Unit
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mBuild.startDraw(canvas, mPaint)
    }

    fun refreshData() {
        val start = System.currentTimeMillis()
        getLayerBuilder().invoke(mBuild)
        Log.d("测试测量耗时", "${System.currentTimeMillis() - start}")
        requestLayout()
    }

}