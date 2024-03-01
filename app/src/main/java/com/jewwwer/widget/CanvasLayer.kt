package com.jewwwer.widget

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
import com.jewwwer.widget.core.*
import com.jewwwer.widget.params.ColumnParams
import com.jewwwer.widget.params.DrawParams
import com.jewwwer.widget.params.RowParams
import com.jewwwer.widget.params.TextParams
import com.kyle.common.kt.extend.initConfig
import com.kyle.common.util.AppStack

/**
 * desc:
 * @author:Jewwwer
 * @Date:2024/2/29
 */
class CanvasLayer {
    open class Builder {
        companion object {
            const val ALIGN_LEFT = 1
            const val ALIGN_TOP = 1 shl 1
            const val ALIGN_RIGHT = 1 shl 2
            const val ALIGN_BOTTOM = 1 shl 3
            const val ALIGN_CENTER = 1 shl 4

            const val MATCH_PARENT = -2F
            const val WRAP_CONTENT = -1F
        }

        var mTypeface: Typeface? = null
        private val mRootLayerList = arrayListOf<DrawCore<*>>()
        fun startInflate() {
            mRootLayerList.clear()
        }

        fun getRootLayer() = mRootLayerList

        open fun Text(text: String, paramsBuild: TextParams.() -> Unit) {
            val params = TextParams()
            paramsBuild.invoke(params)
            mRootLayerList.add(TextDrawCore(text, params))
        }

        open fun Space(paramsBuild: DrawParams.() -> Unit) {
            val params = DrawParams()
            paramsBuild.invoke(params)
            mRootLayerList.add(SpaceDrawCore(params))
        }

        open fun Column(paramsBuild: (ColumnParams.() -> Unit)? = null, builder: Builder.() -> Unit) {
            val params = ColumnParams()
            paramsBuild?.invoke(params)
            mRootLayerList.add(ColumnDrawCore(params).apply {
                builder.invoke(this)
            })
        }

        open fun Row(paramsBuild: (RowParams.() -> Unit)? = null, builder: Builder.() -> Unit) {
            val params = RowParams()
            paramsBuild?.invoke(params)
            mRootLayerList.add(RowDrawCore(params).apply {
                builder.invoke(this)
            })
        }

        fun setTypeface(path: String) {
            if (path.isEmpty()) return
            mTypeface = Typeface.createFromAsset(AppStack.getResumeContext().assets, path)
        }

        fun startDraw(canvas: Canvas?, paint: Paint) {
            val start = System.currentTimeMillis()
            if (canvas == null) return
            mTypeface?.let {
                paint.typeface = it
            }
            mRootLayerList.forEach {
                it.onDraw(canvas, 0F, 0F, mBridge?.getViewWidth() ?: canvas.width.toFloat(), mBridge?.getViewHeight() ?: canvas.height.toFloat(), paint)
            }
            Log.d("测试绘制耗时", "${System.currentTimeMillis() - start}")
        }

        fun getLayerRect(): FloatArray {
            val paint = Paint().initConfig()
            mRootLayerList.forEach {
                return it.calculateRect(null, 0F, 0F, 0F, 0F, paint)
            }
            return floatArrayOf(0F, 0F, 0F, 0F)
        }

        fun findClickItem(x: Float, y: Float): (() -> Unit)? {
            var call: (() -> Unit)? = null
            mRootLayerList.forEach { child ->
                child.findItemClick(x, y)?.let {
                    call = it
                    return@forEach
                }
            }
            return call
        }

        private var mBridge: BuildBridge? = null
        fun setBuildBridge(bridge: BuildBridge) {
            mBridge = bridge
        }

        interface BuildBridge {
            fun getViewWidth(): Float
            fun getViewHeight(): Float
        }
    }
}