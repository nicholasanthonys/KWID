package com.ppb.kwid.Model.Movie

import com.google.gson.annotations.SerializedName
import com.ppb.kwid.Model.Movie.Movie

class GetMoviesResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val movies: MutableList<Movie>,
    @SerializedName("total_pages") val pages: Int
)