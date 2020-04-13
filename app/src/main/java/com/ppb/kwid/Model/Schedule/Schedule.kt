package com.ppb.kwid.Model.Schedule

import com.google.gson.annotations.SerializedName

data class Schedule(
    @SerializedName("cinema_name") var cinema_name: String,
    @SerializedName("cinema_type") var cinema_type: String,
    @SerializedName("ticket_price") var ticket_price: Int,
    @SerializedName("time") var time: MutableList<String>
)