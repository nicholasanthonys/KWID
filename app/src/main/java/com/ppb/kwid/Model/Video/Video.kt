package com.ppb.kwid.Model.Video

import com.google.gson.annotations.SerializedName

data class Video(
    @SerializedName("results") val videoResults: List<Result>
)