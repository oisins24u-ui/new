package com.example.iptvplayer.data

import android.content.Context
import android.content.SharedPreferences

class AuthManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("iptv_auth", Context.MODE_PRIVATE)

    fun saveCredentials(url: String, username: String, pass: String) {
        prefs.edit().apply {
            putString("url", url)
            putString("username", username)
            putString("password", pass)
            apply()
        }
    }

    fun getUrl(): String? = prefs.getString("url", null)
    fun getUsername(): String? = prefs.getString("username", null)
    fun getPassword(): String? = prefs.getString("password", null)

    fun isLoggedIn(): Boolean {
        return !getUrl().isNullOrEmpty() && !getUsername().isNullOrEmpty() && !getPassword().isNullOrEmpty()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}
