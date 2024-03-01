package com.jewwwer.widget

import android.content.Context
import android.util.AttributeSet
import com.kyle.common.kt.extend.skinColor
import com.kyle.common.kt.extend.sp
import com.kyle.common.kt.extend.dp

/**
 * desc:
 * @author:Jewwwer
 * @Date:2024/3/1
 */
class TestDrawLayer(context: Context, attrs: AttributeSet?) : BaseDrawLayerView(context, attrs) {
    override fun getLayerBuilder(): CanvasLayer.Builder.() -> Unit = mBuilder

    private val mBuilder: CanvasLayer.Builder.() -> Unit = {
        startInflate()
        Column(paramsBuild = {
            align = CanvasLayer.Builder.ALIGN_RIGHT
        }) {
            Text("测试文本") {
                textColor = R.color.purple_200.skinColor
                textSize = 15.sp
            }
            Row(paramsBuild = {
                dividerWidth = 3.dp
            }) {
                (0 until 5).forEach {
                    Text("测试$it") {
                        textColor = R.color.teal_200.skinColor
                        textSize = 15.sp
                    }
                }
                Column(paramsBuild = {
                    marginLeft = 10.dp
                }) {
                    (0 until 10).forEach {
                        Text("测试$it") {
                            textColor = R.color.purple_500.skinColor
                            textSize = 15.sp
                        }
                    }
                }
            }
            Row(paramsBuild = {
                align = CanvasLayer.Builder.ALIGN_TOP
            }) {
                (0 until 15).forEach {
                    Text("测试$it") {
                        textColor = R.color.purple_200.skinColor
                        textSize = 15.sp
                    }
                }
                Column {
                    (0 until 16).forEach {
                        Text("测试$it") {
                            if (it == 3) {
                                marginTop = 10.dp
                            }
                            textColor = R.color.purple_500.skinColor
                            textSize = 15.sp
                        }
                    }
                }
            }
            Text("结束") {
                textColor = R.color.teal_200.skinColor
                textSize = 15.sp
            }
        }
    }
}