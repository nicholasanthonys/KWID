package com.ppb.kwid.Model.Video

import com.google.gson.annotations.SerializedName

class VideosResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("videos") val videos: Video
)