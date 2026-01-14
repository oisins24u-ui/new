package com.example.iptvplayer.data.repository

import com.example.iptvplayer.data.AuthManager
import com.example.iptvplayer.data.api.XtreamCodesApi
import com.example.iptvplayer.data.model.Category
import com.example.iptvplayer.data.model.LoginResponse
import com.example.iptvplayer.data.model.SeriesStream
import com.example.iptvplayer.data.model.Stream
import com.example.iptvplayer.data.model.VodStream
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Repository(private val authManager: AuthManager) {
    private var api: XtreamCodesApi? = null

    private fun createApi(url: String): XtreamCodesApi {
         // Ensure correct protocol
         var validUrl = url
         if (!validUrl.startsWith("http://") && !validUrl.startsWith("https://")) {
             validUrl = "http://$validUrl"
         }

         val baseUrl = if (validUrl.endsWith("/")) validUrl else "$validUrl/"
         return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(XtreamCodesApi::class.java)
    }

    suspend fun login(url: String, user: String, pass: String): Response<LoginResponse> {
        val tempApi = createApi(url)
        return tempApi.login(user, pass)
    }

    private fun getApi(): XtreamCodesApi? {
        if (api == null) {
            val url = authManager.getUrl()
            if (url != null) {
                api = createApi(url)
            }
        }
        return api
    }

    suspend fun getLiveCategories(): Response<List<Category>>? {
        val user = authManager.getUsername() ?: return null
        val pass = authManager.getPassword() ?: return null
        return getApi()?.getLiveCategories(user, pass)
    }

    suspend fun getLiveStreams(categoryId: String? = null): Response<List<Stream>>? {
        val user = authManager.getUsername() ?: return null
        val pass = authManager.getPassword() ?: return null
        return getApi()?.getLiveStreams(user, pass, categoryId = categoryId)
    }

    suspend fun getVodCategories(): Response<List<Category>>? {
        val user = authManager.getUsername() ?: return null
        val pass = authManager.getPassword() ?: return null
        return getApi()?.getVodCategories(user, pass)
    }

    suspend fun getVodStreams(categoryId: String? = null): Response<List<VodStream>>? {
        val user = authManager.getUsername() ?: return null
        val pass = authManager.getPassword() ?: return null
        return getApi()?.getVodStreams(user, pass, categoryId = categoryId)
    }

    suspend fun getSeriesCategories(): Response<List<Category>>? {
        val user = authManager.getUsername() ?: return null
        val pass = authManager.getPassword() ?: return null
        return getApi()?.getSeriesCategories(user, pass)
    }

    suspend fun getSeries(categoryId: String? = null): Response<List<SeriesStream>>? {
        val user = authManager.getUsername() ?: return null
        val pass = authManager.getPassword() ?: return null
        return getApi()?.getSeries(user, pass, categoryId = categoryId)
    }
}
