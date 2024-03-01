package com.jewwwer.widget.kt

/**
 * desc:
 * @author:Jewwwer
 * @Date:2024/3/1
 */
fun Int.contain(a: Int): Boolean {
    return this and a == a
}