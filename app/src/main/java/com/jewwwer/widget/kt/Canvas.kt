package com.jewwwer.widget.kt

import android.graphics.*
import android.graphics.drawable.Drawable
import com.kyle.common.kt.extend.dp
import com.kyle.common.kt.extend.getTextHeight
import com.kyle.common.kt.extend.getViewHeight
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/**
 * desc:
 * @author:Jewwwer
 * @Date:2024/2/29
 */
fun Canvas?.draw(builder: CanvasBuilder.() -> Unit) {
    this?.let {
        val build = CanvasBuilder(it)
        builder.invoke(build)
    }
}

class CanvasBuilder(val canvas: Canvas) {
    companion object {
        const val DRAW_CENTER = 1
        const val DRAW_START_TOP = 2
        const val DRAW_END_TOP = 3
        const val DRAW_START_BOTTOM = 4
        const val DRAW_END_BOTTOM = 5
        const val DRAW_CENTER_BOTTOM = 6
        const val DRAW_CENTER_TOP = 7

        const val DRAW_START_CENTER = 8
        const val DRAW_END_CENTER = 9

        const val TRIANGLE_ARROW_TOP = 100
        const val TRIANGLE_ARROW_LEFT = 101
        const val TRIANGLE_ARROW_BOTTOM = 102
        const val TRIANGLE_ARROW_RIGHT = 103
    }

    fun drawText(content: String?, x: Float, y: Float, paint: Paint, direction: Int = DRAW_START_TOP, marginX: Float = 0F, marginY: Float = 0F) {
        if (content.isNullOrEmpty()) return
        if (content.contains("\n")) {
            drawLineFeedText(content, x, y, paint, direction, marginX, marginY)
        } else
            when (direction) {
                DRAW_CENTER -> canvas.drawText(content, 0, content.length, x - paint.measureText(content) / 2 + marginX, y + paint.getTextHeight() / 2 + marginY, paint)
                DRAW_START_TOP -> canvas.drawText(content, 0, content.length, x + marginX, y + paint.getTextHeight() + marginY, paint)
                DRAW_END_TOP -> canvas.drawText(content, 0, content.length, x - paint.measureText(content) - marginX, y + paint.getTextHeight() + marginY, paint)
                DRAW_START_BOTTOM -> canvas.drawText(content, 0, content.length, x + marginX, y - marginY, paint)
                DRAW_END_BOTTOM -> canvas.drawText(content, 0, content.length, x - paint.measureText(content) - marginX, y - marginY, paint)
                DRAW_CENTER_BOTTOM -> canvas.drawText(content, 0, content.length, x - paint.measureText(content) / 2 + marginX, y - marginY, paint)
                DRAW_CENTER_TOP -> canvas.drawText(content, 0, content.length, x - paint.measureText(content) / 2 + marginX, y + paint.getTextHeight() + marginY, paint)
                DRAW_START_CENTER -> canvas.drawText(content, 0, content.length, x + marginX, y + paint.getTextHeight() / 2 + marginY, paint)
                DRAW_END_CENTER -> canvas.drawText(content, 0, content.length, x - paint.measureText(content) - marginX, y + paint.getTextHeight() / 2 + marginY, paint)
            }
    }

    fun drawLineFeedText(content: String?, x: Float, y: Float, paint: Paint, direction: Int = DRAW_START_TOP, marginX: Float = 0F, marginY: Float = 0F) {
        if (content.isNullOrEmpty()) return
        val contentList = content.split("\n")
        val oneHeight = (paint.getViewHeight() + paint.getTextHeight()) / 2
        var cy = when (direction) {
            DRAW_CENTER -> y + (contentList.size - 1) * oneHeight * 0.5F + paint.getTextHeight() * 0.5F
            DRAW_START_TOP -> y + (contentList.size - 1) * oneHeight + paint.getTextHeight()
            DRAW_END_TOP -> y + (contentList.size - 1) * oneHeight + paint.getTextHeight()
            DRAW_START_BOTTOM -> y
            DRAW_END_BOTTOM -> y
            DRAW_CENTER_BOTTOM -> y
            DRAW_CENTER_TOP -> y + (contentList.size - 1) * oneHeight + paint.getTextHeight()
            DRAW_START_CENTER -> y + (contentList.size - 1) * oneHeight * 0.5F + paint.getTextHeight() * 0.5F
            DRAW_END_CENTER -> y + (contentList.size - 1) * oneHeight * 0.5F + paint.getTextHeight() * 0.5F
            else -> y
        }
        contentList.forEachIndexed { index, data ->
            when (direction) {
                DRAW_CENTER -> canvas.drawText(data, 0, data.length, x - paint.measureText(data) / 2 + marginX, cy - (contentList.size - index - 1) * oneHeight + marginY, paint)
                DRAW_START_TOP -> canvas.drawText(data, 0, data.length, x + marginX, cy - (contentList.size - index - 1) * oneHeight + marginY, paint)
                DRAW_END_TOP -> canvas.drawText(data, 0, data.length, x - paint.measureText(data) - marginX, cy - (contentList.size - index - 1) * oneHeight + marginY, paint)
                DRAW_START_BOTTOM -> canvas.drawText(data, 0, data.length, x + marginX, cy - (contentList.size - index - 1) * oneHeight - marginY, paint)
                DRAW_END_BOTTOM -> canvas.drawText(data, 0, data.length, x - paint.measureText(data) - marginX, cy - (contentList.size - index - 1) * oneHeight - marginY, paint)
                DRAW_CENTER_BOTTOM -> canvas.drawText(data, 0, data.length, x - paint.measureText(data) / 2 + marginX, cy - (contentList.size - index - 1) * oneHeight - marginY, paint)
                DRAW_CENTER_TOP -> canvas.drawText(data, 0, data.length, x - paint.measureText(data) / 2 + marginX, cy - (contentList.size - index - 1) * oneHeight + marginY, paint)
                DRAW_START_CENTER -> canvas.drawText(data, 0, data.length, x + marginX, cy - (contentList.size - index - 1) * oneHeight + marginY, paint)
                DRAW_END_CENTER -> canvas.drawText(data, 0, data.length, x - paint.measureText(data) - marginX, cy - (contentList.size - index - 1) * oneHeight + marginY, paint)
            }
        }
    }

    fun drawScaleLineText(contentBuilder: List<(Paint) -> String>?, x: Float, y: Float, paint: Paint, width: Float, marginX: Float = 0F, marginY: Float = 0F) {
        if (contentBuilder.isNullOrEmpty()) return
        val originalScaleX = paint.textScaleX
        val contentList = contentBuilder.map {
            it.invoke(paint) to paint.color
        }
        val mWidth = contentList.sumOf {
            paint.measureText(it.first).toDouble()
        }
        if (width < mWidth) {
            paint.textScaleX = width / mWidth.toFloat()
        }
        var left = x + marginX
        contentList.forEach { content ->
            paint.color = content.second
            canvas.drawText(content.first, 0, content.first.length, left, y + paint.getTextHeight() / 2 + marginY, paint)
            left += paint.measureText(content.first)
        }
        paint.textScaleX = originalScaleX
    }

    fun drawBitmap(bitmap: Bitmap?, x: Float, y: Float, paint: Paint, direction: Int = DRAW_START_TOP, marginX: Float = 0F, marginY: Float = 0F) {
        if (bitmap == null) return
        when (direction) {
            DRAW_CENTER -> canvas.drawBitmap(bitmap, x - bitmap.width / 2 + marginX, y - bitmap.height / 2 + marginY, paint)
            DRAW_START_TOP -> canvas.drawBitmap(bitmap, x + marginX, y + marginY, paint)
            DRAW_END_TOP -> canvas.drawBitmap(bitmap, x - bitmap.width - marginX, y + marginY, paint)
            DRAW_START_BOTTOM -> canvas.drawBitmap(bitmap, x + marginX, y - bitmap.height - marginY, paint)
            DRAW_END_BOTTOM -> canvas.drawBitmap(bitmap, x - bitmap.width - marginX, y - bitmap.height - marginY, paint)
            DRAW_CENTER_BOTTOM -> canvas.drawBitmap(bitmap, x - bitmap.width / 2 + marginX, y - bitmap.height - marginY, paint)
            DRAW_CENTER_TOP -> canvas.drawBitmap(bitmap, x - bitmap.width / 2 + marginX, y + marginY, paint)
            DRAW_START_CENTER -> canvas.drawBitmap(bitmap, x + marginX, y - bitmap.height / 2 + marginY, paint)
            DRAW_END_CENTER -> canvas.drawBitmap(bitmap, x - bitmap.width - marginX, y - bitmap.height / 2 + marginY, paint)
        }
    }

    fun drawBackground(drawable: Drawable?, x: Float, y: Float, direction: Int = DRAW_START_TOP, marginX: Float = 0F, marginY: Float = 0F) {
        if (drawable == null) return
        canvas.save()
        val width = drawable.bounds.width().toFloat()
        val height = drawable.bounds.height().toFloat()
        when (direction) {
            DRAW_CENTER -> canvas.translate(x - width / 2, y - height / 2)
            DRAW_START_TOP -> canvas.translate(x + marginX, y + marginY)
            DRAW_END_TOP -> canvas.translate(x - width - marginX, y + marginY)
            DRAW_START_BOTTOM -> canvas.translate(x + marginX, y - height - marginY)
            DRAW_END_BOTTOM -> canvas.translate(x - width - marginX, y - height - marginY)
            DRAW_CENTER_BOTTOM -> canvas.translate(x - width / 2, y - height - marginY)
            DRAW_CENTER_TOP -> canvas.translate(x - width / 2, y + marginY)
            DRAW_START_CENTER -> canvas.translate(x + marginX, y - height / 2)
            DRAW_END_CENTER -> canvas.translate(x - width - marginX, y - height / 2)
        }
        drawable.draw(canvas)
        canvas.restore()
    }

    fun drawPath(path: Path, paint: Paint) {
        canvas.drawPath(path, paint)
    }

    fun drawLine(x: Float, y: Float, endX: Float, endY: Float, paint: Paint) {
        canvas.drawLine(x, y, endX, endY, paint)
    }

    fun drawArc(x: Float, y: Float, radius: Float, start: Float, sweep: Float, linkCenter: Boolean = false, paint: Paint) {
        canvas.drawArc(x - radius, y - radius, x + radius, y + radius, start, sweep, linkCenter, paint)
    }

    fun drawDot(x: Float?, y: Float?, radius: Float, paint: Paint, direction: Int = DRAW_CENTER, marginX: Float = 0F, marginY: Float = 0F) {
        if (x == null || y == null) return
        if (radius <= 0F) return
        when (direction) {
            DRAW_CENTER -> canvas.drawCircle(x + marginX, y + marginY, radius, paint)
            DRAW_START_TOP -> canvas.drawCircle(x + radius / 2 + marginX, y + radius / 2 + marginY, radius, paint)
            DRAW_END_TOP -> canvas.drawCircle(x - radius / 2 - marginX, y + radius / 2 + marginY, radius, paint)
            DRAW_START_BOTTOM -> canvas.drawCircle(x + radius / 2 + marginX, y - radius / 2 - marginY, radius, paint)
            DRAW_END_BOTTOM -> canvas.drawCircle(x - radius / 2 - marginX, y - radius / 2 - marginY, radius, paint)
            DRAW_CENTER_BOTTOM -> canvas.drawCircle(x + marginX, y - radius / 2 - marginY, radius, paint)
            DRAW_CENTER_TOP -> canvas.drawCircle(x + marginX, y + radius / 2 + marginY, radius, paint)
            DRAW_START_CENTER -> canvas.drawCircle(x + radius / 2 + marginX, y + marginY, radius, paint)
            DRAW_END_CENTER -> canvas.drawCircle(x - radius / 2 - marginX, y + marginY, radius, paint)
        }
    }

    /**
     * 绘制三角形
     * @param width            底边边长
     * @param height           三角形高
     * @param direction        绘制位置
     * @param triangleLocation 三角形朝向(默认顶部朝上)
     */
    private val mTrianglePath = Path()
    fun drawTriangle(x: Float, y: Float, width: Float, height: Float, paint: Paint, direction: Int = DRAW_CENTER_BOTTOM, triangleLocation: Int = TRIANGLE_ARROW_TOP, marginX: Float = 0F, marginY: Float = 0F) {
        if (width <= 0 || height <= 0) return
        mTrianglePath.reset()
        canvas.save()
        when (triangleLocation) {
            TRIANGLE_ARROW_TOP -> {
                mTrianglePath.moveTo(x, y)
                mTrianglePath.rLineTo(width, 0F)
                mTrianglePath.rLineTo(-width / 2, -height)
                mTrianglePath.close()
            }
            TRIANGLE_ARROW_LEFT -> {
                mTrianglePath.moveTo(x, y)
                mTrianglePath.rLineTo(0F, width)
                mTrianglePath.rLineTo(-height, -width / 2)
                mTrianglePath.close()
            }
            TRIANGLE_ARROW_BOTTOM -> {
                mTrianglePath.moveTo(x, y)
                mTrianglePath.rLineTo(width, 0F)
                mTrianglePath.rLineTo(-width / 2, height)
                mTrianglePath.close()
            }
            else -> {
                mTrianglePath.moveTo(x, y)
                mTrianglePath.rLineTo(0F, width)
                mTrianglePath.rLineTo(height, -width / 2)
                mTrianglePath.close()
            }
        }
        when (direction) {
            DRAW_CENTER -> {
                when (triangleLocation) {
                    TRIANGLE_ARROW_TOP -> canvas.translate(-width / 2 + marginX, height / 2 + marginY)
                    TRIANGLE_ARROW_LEFT -> canvas.translate(height / 2 + marginX, -width / 2 + marginY)
                    TRIANGLE_ARROW_BOTTOM -> canvas.translate(-width / 2 + marginX, -height / 2 + marginY)
                    else -> canvas.translate(-height / 2 + marginX, -width / 2 + marginY)
                }
            }
            DRAW_START_TOP -> {
                when (triangleLocation) {
                    TRIANGLE_ARROW_TOP -> canvas.translate(marginX, height + marginY)
                    TRIANGLE_ARROW_LEFT -> canvas.translate(height + marginX, marginY)
                    TRIANGLE_ARROW_BOTTOM -> canvas.translate(marginX, -height + marginY)
                    else -> canvas.translate(marginX, marginY)
                }
            }
            DRAW_END_TOP -> {
                when (triangleLocation) {
                    TRIANGLE_ARROW_TOP -> canvas.translate(-width - marginX, height + marginY)
                    TRIANGLE_ARROW_LEFT -> canvas.translate(-marginX, marginY)
                    TRIANGLE_ARROW_BOTTOM -> canvas.translate(-width - marginX, marginY)
                    else -> canvas.translate(-height - marginX, marginY)
                }
            }
            DRAW_START_BOTTOM -> {
                when (triangleLocation) {
                    TRIANGLE_ARROW_TOP -> canvas.translate(marginX, -marginY)
                    TRIANGLE_ARROW_LEFT -> canvas.translate(height + marginX, -width - marginY)
                    TRIANGLE_ARROW_BOTTOM -> canvas.translate(marginX, -height - marginY)
                    else -> canvas.translate(marginX, -width - marginY)
                }
            }
            DRAW_END_BOTTOM -> {
                when (triangleLocation) {
                    TRIANGLE_ARROW_TOP -> canvas.translate(-width - marginX, -marginY)
                    TRIANGLE_ARROW_LEFT -> canvas.translate(-marginX, -width - marginY)
                    TRIANGLE_ARROW_BOTTOM -> canvas.translate(-width - marginX, -height - marginY)
                    else -> canvas.translate(-height - marginX, -width - marginY)
                }
            }
            DRAW_CENTER_BOTTOM -> {
                when (triangleLocation) {
                    TRIANGLE_ARROW_TOP -> canvas.translate(-width / 2 + marginX, -marginY)
                    TRIANGLE_ARROW_LEFT -> canvas.translate(height / 2 + marginX, -width - marginY)
                    TRIANGLE_ARROW_BOTTOM -> canvas.translate(-width / 2 + marginX, -height - marginY)
                    else -> canvas.translate(-height / 2 + marginX, -width - marginY)
                }
            }
            DRAW_CENTER_TOP -> {
                when (triangleLocation) {
                    TRIANGLE_ARROW_TOP -> canvas.translate(-width / 2 + marginX, height + marginY)
                    TRIANGLE_ARROW_LEFT -> canvas.translate(height / 2 + marginX, marginY)
                    TRIANGLE_ARROW_BOTTOM -> canvas.translate(-width / 2 + marginX, marginY)
                    else -> canvas.translate(-height / 2 + marginX, marginY)
                }
            }
            DRAW_START_CENTER -> {
                when (triangleLocation) {
                    TRIANGLE_ARROW_TOP -> canvas.translate(marginX, height / 2 + marginY)
                    TRIANGLE_ARROW_LEFT -> canvas.translate(height + marginX, -width / 2 + marginY)
                    TRIANGLE_ARROW_BOTTOM -> canvas.translate(marginX, -height / 2 + marginY)
                    else -> canvas.translate(marginX, -width / 2 + marginY)
                }
            }
            DRAW_END_CENTER -> {
                when (triangleLocation) {
                    TRIANGLE_ARROW_TOP -> canvas.translate(-width - marginX, height / 2 + marginY)
                    TRIANGLE_ARROW_LEFT -> canvas.translate(-marginX, -width / 2 + marginY)
                    TRIANGLE_ARROW_BOTTOM -> canvas.translate(-width - marginX, -height / 2 + marginY)
                    else -> canvas.translate(-height - marginX, -width / 2 + marginY)
                }
            }
        }
        canvas.drawPath(mTrianglePath, paint)
        canvas.restore()
    }

    /**
     * 绘制矩形
     * */
    private val mRoundRectPath = Path()
    fun drawRect(x: Float, y: Float, width: Float, height: Float, radiusArray: FloatArray? = null, paint: Paint, direction: Int = DRAW_START_TOP, marginX: Float = 0F, marginY: Float = 0F) {
        mRoundRectPath.reset()
        val realRound = FloatArray(8) {
            when {
                radiusArray == null -> 0F
                radiusArray.size == 2 -> radiusArray[it / 4]
                radiusArray.size == 4 -> radiusArray[it / 2]
                radiusArray.size == 8 -> radiusArray[it]
                else -> 0F
            }
        }
        when (direction) {
            DRAW_CENTER -> {
                if (radiusArray == null)
                    mRoundRectPath.addRect(x - width / 2 + marginX, y - height / 2 + marginY, x + width / 2 + marginX, y + height / 2 + marginY, Path.Direction.CW)
                else
                    mRoundRectPath.addRoundRect(x - width / 2 + marginX, y - height / 2 + marginY, x + width / 2 + marginX, y + height / 2 + marginY, realRound, Path.Direction.CW)
            }
            DRAW_START_TOP -> {
                if (radiusArray == null)
                    mRoundRectPath.addRect(x + marginX, y + marginY, x + width + marginX, y + height + marginY, Path.Direction.CW)
                else
                    mRoundRectPath.addRoundRect(x + marginX, y + marginY, x + width + marginX, y + height + marginY, realRound, Path.Direction.CW)
            }
            DRAW_START_CENTER -> {
                if (radiusArray == null)
                    mRoundRectPath.addRect(x + marginX, y - height / 2 + marginY, x + width + marginX, y + height / 2 + marginY, Path.Direction.CW)
                else
                    mRoundRectPath.addRoundRect(x + marginX, y - height / 2 + marginY, x + width + marginX, y + height / 2 + marginY, realRound, Path.Direction.CW)
            }
            DRAW_END_TOP -> {
                if (radiusArray == null)
                    mRoundRectPath.addRect(x - width - marginX, y + marginY, x - marginX, y + height + marginY, Path.Direction.CW)
                else
                    mRoundRectPath.addRoundRect(x - width - marginX, y + marginY, x - marginX, y + height + marginY, realRound, Path.Direction.CW)
            }
            DRAW_START_BOTTOM -> {
                if (radiusArray == null)
                    mRoundRectPath.addRect(x + marginX, y - height - marginY, x + width + marginX, y - marginY, Path.Direction.CW)
                else
                    mRoundRectPath.addRoundRect(x + marginX, y - height - marginY, x + width + marginX, y - marginY, realRound, Path.Direction.CW)
            }
            DRAW_END_BOTTOM -> {
                if (radiusArray == null)
                    mRoundRectPath.addRect(x - width - marginX, y - height - marginY, x - marginX, y - marginY, Path.Direction.CW)
                else
                    mRoundRectPath.addRoundRect(x - width - marginX, y - height - marginY, x - marginX, y - marginY, realRound, Path.Direction.CW)
            }
            DRAW_CENTER_BOTTOM -> {
                if (radiusArray == null)
                    mRoundRectPath.addRect(x - width / 2 + marginX, y - height - marginY, x + width / 2 + marginX, y - marginY, Path.Direction.CW)
                else
                    mRoundRectPath.addRoundRect(x - width / 2 + marginX, y - height - marginY, x + width / 2 + marginX, y - marginY, realRound, Path.Direction.CW)
            }
            DRAW_CENTER_TOP -> {
                if (radiusArray == null)
                    mRoundRectPath.addRect(x - width / 2 + marginX, y + marginY, x + width / 2 + marginX, y + height + marginY, Path.Direction.CW)
                else
                    mRoundRectPath.addRoundRect(x - width / 2 + marginX, y + marginY, x + width / 2 + marginX, y + height + marginY, realRound, Path.Direction.CW)
            }
        }
        canvas.drawPath(mRoundRectPath, paint)
    }

    /**
     * 绘制箭头
     * 从x,y绘制到endX,endY。箭头在end位置
     * @param width   箭头臂长，可控制箭头大小，默认2dp
     * @param isSolid 是否是实心箭头
     * */
    private val mArrowPath = Path()
    fun drawArrow(x: Float, y: Float, endX: Float, endY: Float, width: Float = 2.dp, isSolid: Boolean = false, paint: Paint) {
        drawLine(x, y, endX, endY, paint)
        canvas.save()
        val angle = atan2(abs(endY - y), abs(endX - x)) * 180F / Math.PI.toFloat()
        val realAngel = when {
            endX > x && endY > y -> -angle
            endX > x && endY < y -> angle
            endX < x && endY > y -> 180F + angle
            endX < x && endY < y -> 180 - angle
            endX == x -> if (endY > y) -90F else if (endY < y) 90F else 0F
            endY == y -> if (endX >= x) 0F else 180F
            else -> 0F
        }
        canvas.rotate(-realAngel, endX, endY)
        val arrowAngle = (40F * Math.PI / 180F).toFloat()
        if (isSolid) {
            mArrowPath.reset()
            mArrowPath.moveTo(endX, endY)
            mArrowPath.lineTo(endX - width * cos(arrowAngle), endY - width * sin(arrowAngle))
            mArrowPath.lineTo(endX - width * cos(arrowAngle), endY + width * sin(arrowAngle))
            mArrowPath.close()
            drawPath(mArrowPath, paint)
        } else {
            drawLine(endX, endY, endX - width * cos(arrowAngle), endY - width * sin(arrowAngle), paint)
            drawLine(endX, endY, endX - width * cos(arrowAngle), endY + width * sin(arrowAngle), paint)
        }
        canvas.restore()
    }

    /**
     * 绘制矩形和三角形拼接的标签
     * */
    private val mTriangleRectPath = Path()
    fun drawTriangleRect(x: Float, y: Float, width: Float, height: Float, paint: Paint, direction: Int = DRAW_CENTER_BOTTOM, marginX: Float = 0F, marginY: Float = 0F, triangleLocation: Int) {
        val dx = width / 3
        val dy = height / 3
        val join = paint.strokeJoin
        paint.strokeJoin = Paint.Join.ROUND
        if (width <= 0 || height <= 0) return
        mTriangleRectPath.reset()
        canvas.save()
        when (triangleLocation) {
            TRIANGLE_ARROW_TOP -> {
                mTriangleRectPath.moveTo(x, y)
                mTriangleRectPath.rLineTo(width, 0F)
                mTriangleRectPath.rLineTo(0F, -height)
                mTriangleRectPath.rLineTo(-width / 2, -dy)
                mTriangleRectPath.rLineTo(-width / 2, dy)
                mTriangleRectPath.close()
            }
            TRIANGLE_ARROW_LEFT -> {
                mTriangleRectPath.moveTo(x, y)
                mTriangleRectPath.rLineTo(0F, width)
                mTriangleRectPath.rLineTo(-height, 0F)
                mTriangleRectPath.rLineTo(-dy, -height / 2)
                mTriangleRectPath.rLineTo(dy, height / 2)
                mTriangleRectPath.close()
            }
            TRIANGLE_ARROW_BOTTOM -> {
                mTriangleRectPath.moveTo(x, y)
                mTriangleRectPath.rLineTo(width, 0F)
                mTriangleRectPath.rLineTo(0F, height)
                mTriangleRectPath.rLineTo(-width / 2, dy)
                mTriangleRectPath.rLineTo(-width / 2, -dy)
                mTriangleRectPath.close()
            }
            else -> {
                mTriangleRectPath.moveTo(x, y)
                mTriangleRectPath.rLineTo(0F, width)
                mTriangleRectPath.rLineTo(height, 0F)
                mTriangleRectPath.rLineTo(dy, -width / 2)
                mTriangleRectPath.rLineTo(-dy, -width / 2)
                mTriangleRectPath.close()
            }
        }
        val translateY = (height + dy)
        when (direction) {
            DRAW_CENTER -> {
                when (triangleLocation) {
                    TRIANGLE_ARROW_TOP -> canvas.translate(-width / 2 + marginX, translateY / 2 + marginY)
                    TRIANGLE_ARROW_LEFT -> canvas.translate(translateY / 2 + marginX, -width / 2 + marginY)
                    TRIANGLE_ARROW_BOTTOM -> canvas.translate(-width / 2 + marginX, -translateY / 2 + marginY)
                    else -> canvas.translate(-translateY / 2 + marginX, -width / 2 + marginY)
                }
            }
            DRAW_START_TOP -> {
                when (triangleLocation) {
                    TRIANGLE_ARROW_TOP -> canvas.translate(marginX, translateY + marginY)
                    TRIANGLE_ARROW_LEFT -> canvas.translate(translateY + marginX, marginY)
                    TRIANGLE_ARROW_BOTTOM -> canvas.translate(marginX, -translateY + marginY)
                    else -> canvas.translate(marginX, marginY)
                }
            }
            DRAW_END_TOP -> {
                when (triangleLocation) {
                    TRIANGLE_ARROW_TOP -> canvas.translate(-width - marginX, translateY + marginY)
                    TRIANGLE_ARROW_LEFT -> canvas.translate(-marginX, marginY)
                    TRIANGLE_ARROW_BOTTOM -> canvas.translate(-width - marginX, marginY)
                    else -> canvas.translate(-translateY - marginX, marginY)
                }
            }
            DRAW_START_BOTTOM -> {
                when (triangleLocation) {
                    TRIANGLE_ARROW_TOP -> canvas.translate(marginX, -marginY)
                    TRIANGLE_ARROW_LEFT -> canvas.translate(translateY + marginX, -width - marginY)
                    TRIANGLE_ARROW_BOTTOM -> canvas.translate(marginX, -translateY - marginY)
                    else -> canvas.translate(marginX, -width - marginY)
                }
            }
            DRAW_END_BOTTOM -> {
                when (triangleLocation) {
                    TRIANGLE_ARROW_TOP -> canvas.translate(-width - marginX, -marginY)
                    TRIANGLE_ARROW_LEFT -> canvas.translate(-marginX, -width - marginY)
                    TRIANGLE_ARROW_BOTTOM -> canvas.translate(-width - marginX, -translateY - marginY)
                    else -> canvas.translate(-translateY - marginX, -width - marginY)
                }
            }
            DRAW_CENTER_BOTTOM -> {
                when (triangleLocation) {
                    TRIANGLE_ARROW_TOP -> canvas.translate(-width / 2 + marginX, -marginY)
                    TRIANGLE_ARROW_LEFT -> canvas.translate(translateY / 2 + marginX, -width - marginY)
                    TRIANGLE_ARROW_BOTTOM -> canvas.translate(-width / 2 + marginX, -translateY - marginY)
                    else -> canvas.translate(-translateY / 2 + marginX, -width - marginY)
                }
            }
            DRAW_CENTER_TOP -> {
                when (triangleLocation) {
                    TRIANGLE_ARROW_TOP -> canvas.translate(-width / 2 + marginX, translateY + marginY)
                    TRIANGLE_ARROW_LEFT -> canvas.translate(translateY / 2 + marginX, marginY)
                    TRIANGLE_ARROW_BOTTOM -> canvas.translate(-width / 2 + marginX, marginY)
                    else -> canvas.translate(-translateY / 2 + marginX, marginY)
                }
            }
            DRAW_START_CENTER -> {
                when (triangleLocation) {
                    TRIANGLE_ARROW_TOP -> canvas.translate(marginX, translateY / 2 + marginY)
                    TRIANGLE_ARROW_LEFT -> canvas.translate(translateY + marginX, -width / 2 + marginY)
                    TRIANGLE_ARROW_BOTTOM -> canvas.translate(marginX, -translateY / 2 + marginY)
                    else -> canvas.translate(marginX, -width / 2 + marginY)
                }
            }
            DRAW_END_CENTER -> {
                when (triangleLocation) {
                    TRIANGLE_ARROW_TOP -> canvas.translate(-width - marginX, translateY / 2 + marginY)
                    TRIANGLE_ARROW_LEFT -> canvas.translate(-marginX, -width / 2 + marginY)
                    TRIANGLE_ARROW_BOTTOM -> canvas.translate(-width - marginX, -translateY / 2 + marginY)
                    else -> canvas.translate(-translateY - marginX, -width / 2 + marginY)
                }
            }
        }
        canvas.drawPath(mTriangleRectPath, paint)
        canvas.restore()
        paint.strokeJoin = join
    }

    fun drawXfermode(drawBackground: () -> Unit, drawForeground: () -> Unit, paint: Paint) {
        val saved = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG)
        drawBackground.invoke()
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
        drawForeground.invoke()
        paint.xfermode = null
        canvas.restoreToCount(saved)
    }
}