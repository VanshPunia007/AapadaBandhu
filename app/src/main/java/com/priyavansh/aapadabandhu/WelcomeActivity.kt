package com.priyavansh.aapadabandhu

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.priyavansh.aapadabandhu.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityWelcomeBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor= Color.BLACK
        setContentView(binding.root)
        binding.signupBtn.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
        binding.loginBtn.setOnClickListener {
            startActivity(Intent(this, LoginScreen::class.java))
        }
    }
}