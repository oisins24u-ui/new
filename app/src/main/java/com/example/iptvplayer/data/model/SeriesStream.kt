package com.example.iptvplayer.data.model

import com.google.gson.annotations.SerializedName

data class SeriesStream(
    @SerializedName("name") val name: String,
    @SerializedName("series_id") val seriesId: Int,
    @SerializedName("cover") val cover: String?
)
