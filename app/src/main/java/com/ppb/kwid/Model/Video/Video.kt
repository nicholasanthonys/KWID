package com.ppb.kwid.Model.Video

import com.google.gson.annotations.SerializedName

data class Video(
    @SerializedName("backdrop_path") val backdrop: String,
    @SerializedName("results") val videoResults: List<Result>
)