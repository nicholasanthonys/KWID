package com.ppb.kwid.Model.Movie

import com.ppb.kwid.API.Api
import com.ppb.kwid.Model.MovieDetail.GetMovieDetailsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MoviesRepository {
    private val api: Api

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(Api::class.java)
    }

    fun getPopularMovies(
        page: Int = 1,
        onSuccess: (movies: MutableList<Movie>) -> Unit,
        onError: () -> Unit
    ) {


        api.getPopularMovies(page = page).enqueue(object : Callback<GetMoviesResponse> {
            override fun onResponse(
                call: Call<GetMoviesResponse>,
                response: Response<GetMoviesResponse>
            ) {
                if (response.isSuccessful) {

                    val responseBody = response.body()

                    if (responseBody != null) {
                        onSuccess.invoke(responseBody.movies)
                        println("================================================")
                        println("page: ${responseBody.page}")
                        println("pages: ${responseBody.pages}")
                        println("================================================")
                    } else {
                        onError.invoke()
                    }
                } else {
                    onError.invoke()
                }
            }

            override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                onError.invoke()
            }
        }
        )
    }

    fun getTopRatedMovies(
        page: Int = 1,
        onSuccess: (movies: MutableList<Movie>) -> Unit,
        onError: () -> Unit
    ) {
        api.getTopRatedMovies(page = page).enqueue(object : Callback<GetMoviesResponse> {
            override fun onResponse(
                call: Call<GetMoviesResponse>,
                response: Response<GetMoviesResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        onSuccess.invoke(responseBody.movies)
                    } else {
                        onError.invoke()
                    }
                } else {
                    onError.invoke()
                }
            }

            override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                onError.invoke()
            }
        })
    }

}