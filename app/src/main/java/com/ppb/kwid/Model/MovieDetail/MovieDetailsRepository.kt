package com.ppb.kwid.Model.MovieDetail

import com.ppb.kwid.API.Api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MovieDetailsRepository {
    private val api: Api

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(Api::class.java)
    }

    fun getMovieDetails(
        id: Long = 1,
        onSuccess: (response: GetMovieDetailsResponse) -> Unit,
        onError: () -> Unit
    ) {
        api.getMovieDetails(id = id)
            .enqueue(object : Callback<GetMovieDetailsResponse> {
                override fun onResponse(
                    call: Call<GetMovieDetailsResponse>,
                    response: Response<GetMovieDetailsResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
//                            Log.d("Movie Details Repo", "MOVIE TITLE: ${responseBody.title}")
//                            Log.d("MOVIE DETAILS Repo", "MOVIE DURATION: ${responseBody.duration}")

                            onSuccess(responseBody)

                        } else {
                            //println("GET CAST ERROR")
                            //Log.d("CAST REPOSITORY", "Failed to get response")
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<GetMovieDetailsResponse>, t: Throwable) {
                    //Log.e("Movie Details", "onFailure", t)
                    onError.invoke()
                }
            })
    }
}