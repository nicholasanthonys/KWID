package com.ppb.kwid

import com.google.gson.annotations.SerializedName

data class GetMoviesResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val movies: MutableList<Movie>,
    @SerializedName("total_pages") val pages: Int
)