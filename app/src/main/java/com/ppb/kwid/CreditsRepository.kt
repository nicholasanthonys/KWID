package com.ppb.kwid

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CreditsRepository {
    private val api: Api

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(Api::class.java)
    }

    fun getCasts(
        id: Long = 1,
        onSuccess: (casts: List<Cast>, crews : List<Crew>) -> Unit,
        onError: () -> Unit
    ) {
        api.getCasts(id = id)
            .enqueue(object : Callback<GetCreditsResponse> {
                override fun onResponse(
                    call: Call<GetCreditsResponse>,
                    response: Response<GetCreditsResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            Log.d("Crew Repository", "CREW: ${responseBody.crews}")
                            onSuccess.invoke(responseBody.casts,responseBody.crews)
                        } else {
                            println("GET CAST ERROR")
                            Log.d("CAST REPOSITORY", "Failed to get response")
                            onError.invoke()
                        }
                    } else {
                        println("GET CAST ERROR id : " + id)
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<GetCreditsResponse>, t: Throwable) {
                    println("GET CAST ERROR")
                    Log.e("Cast", "onFailure", t)
                    onError.invoke()
                }
            })
    }
}