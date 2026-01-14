package com.example.iptvplayer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.iptvplayer.data.AuthManager
import com.example.iptvplayer.data.repository.Repository
import com.example.iptvplayer.databinding.ActivityLoginBinding
import com.example.iptvplayer.ui.login.LoginResult
import com.example.iptvplayer.ui.login.LoginViewModel
import com.example.iptvplayer.ui.login.LoginViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authManager = AuthManager(this)

        if (authManager.isLoggedIn()) {
            navigateToMain()
            finish()
            return
        }

        val repository = Repository(authManager)
        val factory = LoginViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.loginResult.observe(this) { result ->
            when (result) {
                is LoginResult.Success -> {
                    if (binding.rememberMeCheckBox.isChecked) {
                        authManager.saveCredentials(
                            binding.urlEditText.text.toString(),
                            binding.usernameEditText.text.toString(),
                            binding.passwordEditText.text.toString()
                        )
                    }
                    navigateToMain()
                    finish()
                }
                is LoginResult.Error -> {
                    Toast.makeText(this, result.error, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
             binding.loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
             binding.loginButton.isEnabled = !isLoading
        }
    }

    private fun setupListeners() {
        binding.loginButton.setOnClickListener {
            val url = binding.urlEditText.text.toString()
            val user = binding.usernameEditText.text.toString()
            val pass = binding.passwordEditText.text.toString()
            val remember = binding.rememberMeCheckBox.isChecked

            viewModel.login(url, user, pass, remember)
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
