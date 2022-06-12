package com.dicoding.picodiploma.capstoneproject.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.capstoneproject.R
import com.dicoding.picodiploma.capstoneproject.databinding.ActivitySignupBinding
import com.dicoding.picodiploma.capstoneproject.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        binding.tvGotologin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.btnSignup.setOnClickListener {
            binding.pbSignup.visibility = View.VISIBLE
            //showLoading(true)
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmpassword = binding.confirmpasswordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmpassword.isNotEmpty()) {
                binding.pbSignup.visibility = View.VISIBLE
                if (password == confirmpassword) {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            binding.pbSignup.visibility = View.INVISIBLE
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)

                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                            binding.pbSignup.visibility = View.INVISIBLE
                        }
                    }

                } else {
                    Toast.makeText(this, getString(R.string.requiredpassword), Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(this, getString(R.string.password_statement), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
