package com.ppb.kwid

import com.google.gson.annotations.SerializedName

class GetCastsResponse (
    @SerializedName("id") val id : Int,
    @SerializedName("cast") val casts : List<Cast>
)

