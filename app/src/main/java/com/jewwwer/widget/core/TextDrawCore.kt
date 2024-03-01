package com.jewwwer.widget.core

import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import com.jewwwer.widget.kt.CanvasBuilder
import com.jewwwer.widget.kt.contain
import com.jewwwer.widget.kt.draw
import com.jewwwer.widget.params.TextParams
import com.kyle.common.kt.extend.getTextHeight
import com.kyle.common.kt.extend.getViewHeight
import com.kyle.common.kt.extend.sp
import kotlin.math.max

/**
 * desc:
 * @author:Jewwwer
 * @Date:2024/2/29
 */
class TextDrawCore(val content: String, params: TextParams) : DrawCore<TextParams>(params) {
    override fun onDraw(canvas: Canvas?, left: Float, top: Float, right: Float, bottom: Float, paint: Paint) {
        Log.d("测试文本绘制", "content:${content},left:$left,top:$top,right:$right,bottom:$bottom")
        val textColor = params.textColor
        val textSize = params.textSize
        if (textColor != 0) {
            paint.color = textColor
        }
        if (textSize != 0F) {
            paint.textSize = autoTextSize(paint, textSize)
        }
        val align = params.textAlign
        val drawable = params.drawable
        val drawablePadding = params.drawablePadding
        val drawableWidth = drawable?.bounds?.width() ?: 0
        val drawableHeight = drawable?.bounds?.height() ?: 0
        val background = params.background
        val rect = getDrawParams().getParamsRect()
        background?.let {
            it.setBounds(rect.left.toInt(), rect.top.toInt(), rect.right.toInt(), rect.bottom.toInt())
            if (canvas != null)
                it.draw(canvas)
        }
        val tx = if (drawableWidth != 0) drawableWidth + drawablePadding else 0F

        canvas?.draw {
            when {
                align.contain(ALIGN_CENTER) -> {
                    drawText(content, (left + right) / 2 - tx / 2, (top + bottom) / 2, paint, CanvasBuilder.DRAW_CENTER)
                    drawBackground(drawable, (left + right) / 2 + tx / 2, (top + bottom) / 2, CanvasBuilder.DRAW_CENTER)
                }
                align.contain(ALIGN_LEFT) -> {
                    if (align.contain(ALIGN_TOP)) {
                        val centerTop = top + (max(drawableHeight.toFloat(), paint.getTextHeight()) - paint.getTextHeight()) / 2
                        drawText(content, left, centerTop, paint, CanvasBuilder.DRAW_START_TOP)
                        drawBackground(drawable, paint.measureText(content) + drawablePadding, centerTop + paint.getTextHeight() / 2, CanvasBuilder.DRAW_START_CENTER)
                    } else {
                        val centerBottom = bottom - (max(drawableHeight.toFloat(), paint.getTextHeight()) - paint.getTextHeight()) / 2
                        drawText(content, left, centerBottom, paint, CanvasBuilder.DRAW_START_BOTTOM)
                        drawBackground(drawable, paint.measureText(content) + drawablePadding, centerBottom - paint.getTextHeight() / 2, CanvasBuilder.DRAW_START_CENTER)
                    }
                }
                align.contain(ALIGN_RIGHT) -> {
                    if (align.contain(ALIGN_TOP)) {
                        val centerTop = top + (max(drawableHeight.toFloat(), paint.getTextHeight()) - paint.getTextHeight()) / 2
                        drawText(content, right - tx, centerTop, paint, CanvasBuilder.DRAW_START_TOP)
                        drawBackground(drawable, right, centerTop + paint.getTextHeight() / 2, CanvasBuilder.DRAW_START_CENTER)
                    } else {
                        val centerBottom = bottom - (max(drawableHeight.toFloat(), paint.getTextHeight()) - paint.getTextHeight()) / 2
                        drawText(content, right - tx, centerBottom, paint, CanvasBuilder.DRAW_END_BOTTOM)
                        drawBackground(drawable, right, centerBottom - paint.getTextHeight() / 2, CanvasBuilder.DRAW_END_CENTER)
                    }
                }
            }
        }
    }

    private fun autoTextSize(paint: Paint, textsize: Float): Float {
        paint.textSize = textsize
        val textWidth = if (content.contains("\n")) {
            content.run {
                val list = this.split("\n")
                list.maxOf {
                    paint.measureText(it)
                }
            }
        } else paint.measureText(content)
        val drawable = params.drawable
        val drawablePadding = params.drawablePadding
        val drawableWidth = drawable?.bounds?.width() ?: 0
        val width = params.width
        val tx = if (drawableWidth != 0) drawableWidth + drawablePadding else 0F
        if (width != WRAP_CONTENT && (width - tx) < textWidth) {
            return autoTextSize(paint, textsize - 0.5F.sp)
        }
        return textsize
    }

    override fun calculateRect(canvas: Canvas?, left: Float, top: Float, right: Float, bottom: Float, paint: Paint): FloatArray {
        val width = params.width
        val height = params.height
        if (params.textSize != 0F) {
            paint.textSize = params.textSize
        }
        val viewHeight = paint.getViewHeight()
        val textWidth = paint.measureText(content)
        val drawableWidth = params.drawable?.bounds?.width()?.run {
            if (this == 0)
                0F
            else
                this + params.drawablePadding
        } ?: 0F
        return floatArrayOf(left, top, left + if (width == WRAP_CONTENT) (textWidth + drawableWidth) else width, top + if (height == WRAP_CONTENT) viewHeight else height)
    }
}