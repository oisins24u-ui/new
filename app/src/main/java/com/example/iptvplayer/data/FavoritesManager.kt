package com.example.iptvplayer.data

import android.content.Context
import android.content.SharedPreferences

class FavoritesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("iptv_favorites", Context.MODE_PRIVATE)

    fun addFavorite(streamId: String) {
        val favorites = getFavorites().toMutableSet()
        favorites.add(streamId)
        prefs.edit().putStringSet("favorites", favorites).apply()
    }

    fun removeFavorite(streamId: String) {
        val favorites = getFavorites().toMutableSet()
        favorites.remove(streamId)
        prefs.edit().putStringSet("favorites", favorites).apply()
    }

    fun isFavorite(streamId: String): Boolean {
        return getFavorites().contains(streamId)
    }

    fun getFavorites(): Set<String> {
        return prefs.getStringSet("favorites", emptySet()) ?: emptySet()
    }
}
