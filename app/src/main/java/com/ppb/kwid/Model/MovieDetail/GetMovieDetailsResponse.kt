package com.ppb.kwid.Model.MovieDetail

import com.google.gson.annotations.SerializedName
import com.ppb.kwid.Model.Genre.Genres

class GetMovieDetailsResponse (
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("vote_average") val rating: Float,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("runtime") val duration : Int,
    @SerializedName("genres") val genres : List<Genres>
)