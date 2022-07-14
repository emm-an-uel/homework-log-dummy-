package com.example.homeworklog_dummy

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import com.example.homeworklog_dummy.databinding.ActivityPopUpWindowBinding
import kotlinx.android.synthetic.main.activity_pop_up_window.*

class PopUpWindow : AppCompatActivity() {

    private lateinit var binding: ActivityPopUpWindowBinding

    private var popupTitle = "test"
    private var popupText = "wee"
    private var popupButton = "close"
    private var darkStatusBar = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)

        binding = ActivityPopUpWindowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set data
        binding.popupWindowTitle.text = popupTitle
        binding.popupWindowText.text = popupText
        binding.popupWindowButton.text = popupButton

        // getting data
        val bundle = intent.extras
        popupTitle = bundle?.getString("popuptitle", "Title") ?: ""
        popupText = bundle?.getString("popuptext", "Text") ?: ""
        popupButton = bundle?.getString("popupbtn", "Button") ?: ""
        darkStatusBar = bundle?.getBoolean("darkstatusbar", false) ?: false

        // delete self when button pressed
        popup_window_button.setOnClickListener() {
            onBackPressed()
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}