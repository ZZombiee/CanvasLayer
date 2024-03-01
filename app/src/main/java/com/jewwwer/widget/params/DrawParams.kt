package com.jewwwer.widget.params

import android.graphics.RectF
import android.graphics.drawable.Drawable
import com.jewwwer.widget.CanvasLayer

/**
 * desc:
 * @author:Jewwwer
 * @Date:2024/2/29
 */
open class DrawParams {
    var width = CanvasLayer.Builder.WRAP_CONTENT
    var height = CanvasLayer.Builder.WRAP_CONTENT
    var marginLeft = 0F
    var marginRight = 0F
    var marginTop = 0F
    var marginBottom = 0F
    var background: Drawable? = null
    var clickCall: (() -> Unit)? = null
    private val rect = RectF()

    fun setRect(left: Float, top: Float, right: Float, bottom: Float) {
        rect.set(left, top, right, bottom)
    }

    fun getParamsRect() = rect
}