package com.priyavansh.aapadabandhu

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.priyavansh.aapadabandhu.databinding.ActivityLoginScreenBinding

class LoginScreen : AppCompatActivity() {
    private val binding by lazy{
        ActivityLoginScreenBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor=Color.BLACK
        setContentView(binding.root)

        binding.loginbtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}