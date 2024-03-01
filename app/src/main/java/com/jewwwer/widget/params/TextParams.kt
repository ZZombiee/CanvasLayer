package com.jewwwer.widget.params

import android.graphics.drawable.Drawable
import com.jewwwer.widget.CanvasLayer
import com.jewwwer.widget.R
import com.kyle.common.kt.extend.dp
import com.kyle.common.kt.extend.skinColor

/**
 * desc:
 * @author:Jewwwer
 * @Date:2024/2/29
 */
class TextParams : DrawParams() {
    var drawable: Drawable? = null
    var drawablePadding = 3.dp
    var textSize = 12.dp
    var textColor = R.color.black.skinColor
    var textAlign = CanvasLayer.Builder.ALIGN_LEFT or CanvasLayer.Builder.ALIGN_TOP
}