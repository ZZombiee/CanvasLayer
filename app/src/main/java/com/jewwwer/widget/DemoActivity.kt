package com.jewwwer.widget

import android.app.Activity
import android.os.Bundle
import android.widget.Button

/**
 * desc:
 * @author:Jewwwer
 * @Date:2024/3/1
 */
class DemoActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_demo)
        val btn = findViewById<Button>(R.id.btn_test)
        val layer = findViewById<TestDrawLayer>(R.id.test_layer)
        btn.setOnClickListener {
            layer.refreshData()
        }
    }
}