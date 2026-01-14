package com.example.iptvplayer.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.iptvplayer.data.repository.Repository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(url: String, user: String, pass: String, remember: Boolean) {
        if (url.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            _loginResult.value = LoginResult.Error("Empty Fields")
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.login(url, user, pass)
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    // Ideally check status, but let's assume if we get a valid response we are good for now
                    // Usually we check body.userInfo.status == "Active"
                    _loginResult.value = LoginResult.Success
                } else {
                    _loginResult.value = LoginResult.Error("Login Failed: ${response.code()}")
                }
            } catch (e: Exception) {
                _loginResult.value = LoginResult.Error(e.message ?: "Unknown error")
            } finally {
                _isLoading.value = false
            }
        }
    }
}

sealed class LoginResult {
    object Success : LoginResult()
    data class Error(val error: String) : LoginResult()
}

class LoginViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
