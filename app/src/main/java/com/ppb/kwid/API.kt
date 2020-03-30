package com.ppb.kwid

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String = "d7c23b0b88eb491c20a317ecfee47db3",
        @Query("page") page: Int
    ): Call<GetMoviesResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String = "d7c23b0b88eb491c20a317ecfee47db3",
        @Query("page") page: Int
    ): Call<GetMoviesResponse>

    @GET("movie/{movie_id}/credits")
    fun getCasts(
        @Path("movie_id") id: Long,
        @Query("api_key") apiKey: String = "d7c23b0b88eb491c20a317ecfee47db3"
    ): Call<GetCastsResponse>
}