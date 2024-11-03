package com.priyavansh.aapadabandhu

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.priyavansh.aapadabandhu.databinding.ActivitySignupBinding
import com.priyavansh.aapadabandhu.models.User
import com.priyavansh.aapadabandhu.utils.USER_NODE
import com.priyavansh.aapadabandhu.utils.USER_PROFILE_FOLDER
import com.priyavansh.aapadabandhu.utils.uploadImage

class SignupActivity : AppCompatActivity() {
    val binding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }

    lateinit var user: User
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImage(uri, USER_PROFILE_FOLDER) {
                if (it != null) {
                    user.image = it
                    binding.profileImage.setImageURI(uri)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor=Color.BLACK
        setContentView(binding.root)
        user = User()
        binding.login.text = Html.fromHtml(
            "<font color=#023F81>Already have an account? </font>" + "<font color=#1E88E5> Login</font>"
        )
        binding.login.setOnClickListener {
            startActivity(Intent(this, LoginScreen::class.java))
            finish()
        }


        binding.profileImage.setOnClickListener {
            launcher.launch("image/*")
        }

        binding.signupBtn.setOnClickListener {
            val name = binding.name.editText?.text.toString()
            val email = binding.email.editText?.text.toString()
            val password = binding.password.editText?.text.toString()
            val aadhar = binding.aadhar.editText?.text.toString()
            if ((name == "") or (email == "") || password == "" || aadhar == "") {
                Toast.makeText(
                    this@SignupActivity, "Please fill required information", Toast.LENGTH_SHORT
                ).show()
            } else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { result ->
                        if (result.isSuccessful) {
                            user.name = name
                            user.email = email
                            user.password = password
                            user.aadhar = aadhar

                            Firebase.firestore.collection(USER_NODE)
                                .document(Firebase.auth.currentUser!!.uid).set(user)
                                .addOnSuccessListener {
                                    startActivity(
                                        Intent(
                                            this@SignupActivity, MainActivity::class.java
                                        )
                                    )
                                    finish()
                                }
                        } else {
                            Toast.makeText(
                                this@SignupActivity,
                                result.exception?.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}