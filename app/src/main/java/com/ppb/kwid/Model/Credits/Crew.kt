package com.ppb.kwid.Model.Credits

import com.google.gson.annotations.SerializedName

data class Crew (
    @SerializedName("name") val name : String,
    @SerializedName("job") val job : String
)