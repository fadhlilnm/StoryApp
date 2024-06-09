package com.dicoding.storyapp.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.storyapp.data.response.RegisterResponse
import com.dicoding.storyapp.databinding.ActivityRegisterBinding
import com.dicoding.storyapp.ui.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {
    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
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
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            binding.loadingProgressBar.visibility = View.VISIBLE

            viewModel.register(name, email, password)
        }
    }

    private fun observeViewModel() {
        viewModel.registerResult.observe(this) { response ->
            binding.loadingProgressBar.visibility = View.GONE
            if (response != null && response.error == false) {
                showRegistrationSuccessDialog(response)
            } else {
                showRegistrationErrorDialog()
            }
        }
    }

    private fun showRegistrationSuccessDialog(response: RegisterResponse) {
        AlertDialog.Builder(this).apply {
            setTitle("Selamat!")
            setMessage("Pengguna berhasil dibuat!")
            setPositiveButton("Lanjut") { _, _ ->
                finish()
            }
            create()
            show()
        }
    }

    private fun showRegistrationErrorDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Registrasi Gagal")
            setMessage("Mohon masukkan email dan password yang benar!")
            setPositiveButton("Kembali") { _, _ -> }
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
        val nameTextViewAnimation = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayoutAnimation = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextViewAnimation = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayoutAnimation = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextViewAnimation = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayoutAnimation = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signupButtonAnimation = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                titleAnimation,
                nameTextViewAnimation,
                nameEditTextLayoutAnimation,
                emailTextViewAnimation,
                emailEditTextLayoutAnimation,
                passwordTextViewAnimation,
                passwordEditTextLayoutAnimation,
                signupButtonAnimation
            )
            startDelay = 100
        }.start()
    }
}