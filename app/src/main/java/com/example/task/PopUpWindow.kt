package com.example.task

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import com.example.task.databinding.WindowPopUpBinding

class PopUpWindow : AppCompatActivity() {
    private lateinit var binding: WindowPopUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0,0)
        binding = WindowPopUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}