package com.jewwwer.widget.core

import android.graphics.Canvas
import android.graphics.Paint
import com.jewwwer.widget.params.DrawParams

/**
 * desc:
 * @author:Jewwwer
 * @Date:2024/2/29
 */
class SpaceDrawCore(params: DrawParams) : DrawCore<DrawParams>(params) {
    override fun calculateRect(canvas: Canvas?, left: Float, top: Float, right: Float, bottom: Float, paint: Paint): FloatArray {
        val width = params.width
        val height = params.height
        return floatArrayOf(left, top, left + if (width == WRAP_CONTENT) 0F else width, top + if (height == WRAP_CONTENT) 0F else height)
    }

}