package com.jewwwer.widget.core

import android.graphics.Canvas
import android.graphics.Paint
import com.jewwwer.widget.params.ColumnParams
import kotlin.math.max

/**
 * desc:
 * @author:Jewwwer
 * @Date:2024/2/29
 */
class ColumnDrawCore(params: ColumnParams) : GroupDrawCore<ColumnParams>(params) {
    override fun onDraw(canvas: Canvas?, left: Float, top: Float, right: Float, bottom: Float, paint: Paint) {
        mLayerList.forEach {
            val childRect = it.getDrawParams().getParamsRect()
            it.onDraw(canvas, childRect.left, childRect.top, childRect.right, childRect.bottom, paint)
        }
    }

    override fun calculateRect(canvas: Canvas?, left: Float, top: Float, right: Float, bottom: Float, paint: Paint): FloatArray {
        var newRight = 0F
        var newBottom = top
        var location: FloatArray
        val width = params.width
        val align = params.align
        val dividerHeight = params.dividerHeight
        mLayerList.forEachIndexed { index, it ->
            newBottom += it.getDrawParams().marginTop
            //计算每个子组件的大小
            location = it.calculateRect(canvas, left, newBottom, right, bottom, paint)
            val childHeight = location[3] - location[1]
            //设置子控件的方位
            it.getDrawParams().setRect(left, newBottom , location[2], newBottom + childHeight)
            //设置子控件的响应区域
            it.setRectLocation(left, newBottom, location[2], newBottom + childHeight)
            //更新right大小
            if (location.size > 2) {
                newRight = max(newRight, location[2])
            }
            //更新Bottom大小
            if (location.size > 3) {
                newBottom += childHeight
            }
            newBottom += it.getDrawParams().marginBottom
            if (index < mLayerList.size) {
                newBottom += dividerHeight
            }
        }
        //如果是右对齐，那么需要对子控件重新布局一下
        if (align == ALIGN_RIGHT) {
            mLayerList.forEach {
                val childRect = it.getDrawParams().getParamsRect()
                val childWidth = childRect.run { this.right - this.left }
                //如果原区域和新区域不对应，那么需要重新进行计算并覆盖Rect
                if (childRect.left != newRight - childWidth) {
                    it.calculateRect(canvas, newRight - childWidth, childRect.top, newRight, childRect.bottom, paint)
                }
                it.getDrawParams().setRect(newRight - childWidth, childRect.top, newRight, childRect.bottom)
            }
        }
        return floatArrayOf(
            left,
            top,
            when (width) {
                MATCH_PARENT -> right
                WRAP_CONTENT -> newRight
                else -> width + left
            },
            newBottom
        )
    }
}