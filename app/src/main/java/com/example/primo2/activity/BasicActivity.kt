package com.example.primo2.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class BasicActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) // 가로모드 금지하는건데 일단 보류
    }
}