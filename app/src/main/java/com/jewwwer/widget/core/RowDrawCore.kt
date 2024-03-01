package com.jewwwer.widget.core

import android.graphics.Canvas
import android.graphics.Paint
import com.jewwwer.widget.kt.contain
import com.jewwwer.widget.params.RowParams
import kotlin.math.max

/**
 * desc:
 * @author:Jewwwer
 * @Date:2024/2/29
 */
class RowDrawCore(params: RowParams) : GroupDrawCore<RowParams>(params) {
    override fun onDraw(canvas: Canvas?, left: Float, top: Float, right: Float, bottom: Float, paint: Paint) {
        mLayerList.forEach {
            val childRect = it.getDrawParams().getParamsRect()
            it.onDraw(canvas, childRect.left, childRect.top, childRect.right, childRect.bottom, paint)
        }
    }

    override fun calculateRect(canvas: Canvas?, left: Float, top: Float, right: Float, bottom: Float, paint: Paint): FloatArray {
        val height = params.height
        val dividerWidth = params.dividerWidth
        var newRight = left
        var newBottom = top
        var location: FloatArray
        val align = params.align
        mLayerList.forEachIndexed { index, it ->
            newRight += it.getDrawParams().marginLeft
            //计算每个子组件的大小
            location = it.calculateRect(canvas, newRight, top, right, bottom, paint)
            val childWidth = location[2] - location[0]
            //设置子控件的方位
            it.getDrawParams().setRect(newRight, top, newRight + childWidth, location[3])
            //设置子控件的响应区域
            it.setRectLocation(newRight, top, newRight + childWidth, location[3])
            //更新right大小
            if (location.size > 2) {
                newRight += childWidth
            }
            if (index < mLayerList.size) {
                newRight += dividerWidth
            }
            newRight += it.getDrawParams().marginRight
            //更新Bottom大小
            if (location.size > 3) {
                newBottom = max(newBottom, location[3])
            }
        }
        //如果是下对齐，那么需要对子控件重新布局一下
        if (align.contain(ALIGN_BOTTOM)) {
            mLayerList.forEach {
                val childRect = it.getDrawParams().getParamsRect()
                val childHeight = childRect.run { this.bottom - this.top }
                //如果原区域和新区域不对应，那么需要重新进行计算并覆盖Rect
                if (childRect.top != newBottom - childHeight) {
                    it.calculateRect(canvas, childRect.left, newBottom - childHeight, childRect.right, newBottom, paint)
                }
                it.getDrawParams().setRect(childRect.left, newBottom - childHeight, childRect.right, newBottom)
            }
        }
        return floatArrayOf(left, top, newRight, if (height == WRAP_CONTENT) newBottom else (top + height))
    }
}