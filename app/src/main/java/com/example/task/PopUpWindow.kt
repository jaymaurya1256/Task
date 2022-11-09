package com.example.task

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import com.example.task.databinding.WindowPopUpBinding

class PopUpWindow : AppCompatActivity() {
    private lateinit var binding: WindowPopUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WindowPopUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.infoContent.alpha = 0f
        binding.infoContent.animate().alpha(1F)
            .setDuration(1000)
            .setInterpolator(DecelerateInterpolator())
            .start()
        val header = binding.animationForInfoHeader
        header.setAnimation(R.raw.info_header)
        header.loop(true)
        header.playAnimation()
        val footer = binding.animationForInfoFooter
        footer.setAnimation(R.raw.info_footer)
        footer.loop(true)
        footer.playAnimation()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        binding.infoContent.animate().alpha(0f)
            .setDuration(1000)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }
}