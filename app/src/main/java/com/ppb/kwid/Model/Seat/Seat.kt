package com.ppb.kwid.Model.Seat

import com.google.gson.annotations.SerializedName

data class Seat(
    @SerializedName("seatNumber") var seatNumber: String,
    @SerializedName("isEmpty") var isEmpty: Boolean,
    @SerializedName("isSelected") var isSelected: Boolean
)