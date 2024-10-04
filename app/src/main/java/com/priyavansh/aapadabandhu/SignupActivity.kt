package com.priyavansh.aapadabandhu

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.priyavansh.aapadabandhu.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    val binding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor=Color.BLACK
        setContentView(binding.root)

        binding.login.text = Html.fromHtml(
            "<font color=#023F81>Already have an account? </font>" + "<font color=#1E88E5> Login</font>"
        )
        binding.login.setOnClickListener {
            startActivity(Intent(this, LoginScreen::class.java))
        }
    }
}