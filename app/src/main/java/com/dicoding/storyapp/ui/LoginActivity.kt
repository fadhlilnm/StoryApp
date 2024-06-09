package com.dicoding.storyapp.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.storyapp.data.preference.UserModel
import com.dicoding.storyapp.data.response.LoginResponse
import com.dicoding.storyapp.databinding.ActivityLoginBinding
import com.dicoding.storyapp.ui.viewmodel.LoginViewModel
import com.dicoding.storyapp.ui.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeView()
        initializeActions()
        animateViews()
        observeViewModel()
    }

    private fun initializeView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()

        binding.emailEditText.setValidationFunction({ validateEmail(it) }, "Format email tidak valid")
        binding.passwordEditText.setValidationFunction({ validatePassword(it) }, "Password minimal 8 karakter")
    }

    private fun validateEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(Regex(emailPattern))
    }

    private fun validatePassword(password: String): Boolean {
        return password.length >= 8
    }

    private fun initializeActions() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            binding.loadingProgressBar.visibility = View.VISIBLE

            viewModel.login(email, password)
        }
    }

    private fun observeViewModel() {
        viewModel.loginResult.observe(this) { response ->
            binding.loadingProgressBar.visibility = View.INVISIBLE
            if (response != null && response.error == false) {
                showLoginSuccessDialog(response)
            } else {
                showLoginErrorDialog()
            }
        }
    }

    private fun showLoginErrorDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Login Gagal")
            setMessage("Mohon masukkan email dan password yang benar!")
            setPositiveButton("Kembali") { _, _ -> }
            create()
            show()
        }
    }

    private fun showLoginSuccessDialog(response: LoginResponse) {
        AlertDialog.Builder(this).apply {
            setTitle("Sukses!")
            setMessage("Pengguna berhasil login!")
            setPositiveButton("Lanjutkan") { _, _ ->
                val userModel = response.loginResult?.let {
                    UserModel(it.name ?: "", it.token ?: "")
                }
                userModel?.let { viewModel.saveSession(it) }
                val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun animateViews() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val titleAnimation = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val messageAnimation = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextViewAnimation = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayoutAnimation = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextViewAnimation = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayoutAnimation = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val loginButtonAnimation = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                titleAnimation,
                messageAnimation,
                emailTextViewAnimation,
                emailEditTextLayoutAnimation,
                passwordTextViewAnimation,
                passwordEditTextLayoutAnimation,
                loginButtonAnimation
            )
            startDelay = 100
        }.start()
    }
}