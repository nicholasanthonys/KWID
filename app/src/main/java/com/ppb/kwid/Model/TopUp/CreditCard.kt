package com.ppb.kwid.Model.TopUp

import com.google.gson.annotations.SerializedName

data class CreditCard(
    @SerializedName("name") val name: String,
    @SerializedName("image_url") val image_url: String,
    @SerializedName("url") val url: String
)