package com.ppb.kwid.Model.Notification

import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("type") val type: String,
    @SerializedName("header") val header: String,
    @SerializedName("body") val body: String,
    @SerializedName("date") val date: String
)