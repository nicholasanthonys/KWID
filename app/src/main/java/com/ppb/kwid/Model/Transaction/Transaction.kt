package com.ppb.kwid.Model.Transaction

import com.google.gson.annotations.SerializedName

data class Transaction(
    @SerializedName("movie_poster") var movie_poster: String,
    @SerializedName("movie_title") var movie_title: String,
    @SerializedName("cinema_name") var cinema_name: String,
    @SerializedName("schedule") var schedule: String,
    @SerializedName("picked_seat") var picked_seat: MutableList<String>,
    @SerializedName("ticket_price") var ticket_price: Int,
    @SerializedName("time") var time: String,
    @SerializedName("service_fee") var service_fee: Int
)