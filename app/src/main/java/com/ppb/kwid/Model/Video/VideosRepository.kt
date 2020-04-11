package com.ppb.kwid.Model.Video

import android.util.Log
import com.ppb.kwid.API.Api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object VideosRepository {
    private val api: Api

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(Api::class.java)
    }

    fun getMovieVideos(
        id: Long = 1,
        onSuccess: (response: VideosResponse) -> Unit,
        onError: () -> Unit
    ) {
        api.getMovieVideos(id = id)
            .enqueue(object : Callback<VideosResponse> {
                override fun onResponse(
                    call: Call<VideosResponse>,
                    response: Response<VideosResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            var id = responseBody.id
                            var videoResponse = responseBody
                            var backdrop = responseBody.backdropPath

                            onSuccess(videoResponse)

                        } else {
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<VideosResponse>, t: Throwable) {
                    Log.e("Movie Details", "onFailure", t)
                    onError.invoke()
                }
            })
    }
}