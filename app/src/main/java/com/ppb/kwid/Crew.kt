package com.ppb.kwid

import com.google.gson.annotations.SerializedName

data class Crew (
    @SerializedName("name") val name : String,
    @SerializedName("job") val job : String
)