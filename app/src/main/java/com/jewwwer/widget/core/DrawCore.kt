package com.jewwwer.widget.core

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.jewwwer.widget.CanvasLayer
import com.jewwwer.widget.params.DrawParams

/**
 * desc:
 * @author:Jewwwer
 * @Date:2024/2/29
 */
abstract class DrawCore<T : DrawParams>(pa: T) : CanvasLayer.Builder() {
    protected val params: T = pa
    private val mRect = RectF()
    open fun findItemClick(x: Float, y: Float): (() -> Unit)? {
        if (x < mRect.left || y < mRect.top) return null
        if (x > mRect.right || y > mRect.bottom) return null
        return params.clickCall
    }

    fun getDrawParams() = params

    fun setRectLocation(left: Float, top: Float, right: Float, bottom: Float) {
        mRect.set(left, top, right, bottom)
    }

    open fun onDraw(canvas: Canvas?, left: Float, top: Float, right: Float, bottom: Float, paint: Paint) {
    }

    abstract fun calculateRect(canvas: Canvas?, left: Float, top: Float, right: Float, bottom: Float, paint: Paint): FloatArray
}


