package com.jewwwer.widget.core

import com.jewwwer.widget.CanvasLayer
import com.jewwwer.widget.params.ColumnParams
import com.jewwwer.widget.params.DrawParams
import com.jewwwer.widget.params.RowParams
import com.jewwwer.widget.params.TextParams


/**
 * desc:
 * @author:Jewwwer
 * @Date:2024/2/29
 */
abstract class GroupDrawCore<T : DrawParams>(params: T) : DrawCore<T>(params) {
    val mLayerList = arrayListOf<DrawCore<*>>()
    override fun Text(text: String, paramsBuild: TextParams.() -> Unit) {
        val params = TextParams()
        paramsBuild.invoke(params)
        mLayerList.add(TextDrawCore(text, params))
    }

    override fun Space(paramsBuild: DrawParams.() -> Unit) {
        val params = DrawParams()
        paramsBuild.invoke(params)
        mLayerList.add(SpaceDrawCore(params))
    }

    override fun Column(paramsBuild: (ColumnParams.() -> Unit)?, builder: CanvasLayer.Builder.() -> Unit) {
        val params = ColumnParams()
        paramsBuild?.invoke(params)
        mLayerList.add(ColumnDrawCore(params).apply {
            builder.invoke(this)
        })
    }

    override fun Row(paramsBuild: (RowParams.() -> Unit)?, builder: CanvasLayer.Builder.() -> Unit) {
        val params = RowParams()
        paramsBuild?.invoke(params)
        mLayerList.add(RowDrawCore(params).apply {
            builder.invoke(this)
        })
    }

    override fun findItemClick(x: Float, y: Float): (() -> Unit)? {
        var call: (() -> Unit)? = null
        mLayerList.forEach { child ->
            child.findItemClick(x, y)?.let {
                call = it
                return@forEach
            }
        }
        return call ?: super.findItemClick(x, y)
    }
}