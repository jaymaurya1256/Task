package com.example.task

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.task.databinding.WindowPopUpBinding

class PopUpWindow : AppCompatActivity() {
    private lateinit var binding: WindowPopUpBinding
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = WindowPopUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}