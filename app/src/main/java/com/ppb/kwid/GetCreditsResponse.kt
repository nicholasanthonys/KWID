package com.ppb.kwid

import com.google.gson.annotations.SerializedName

class GetCreditsResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("cast") val casts: List<Cast>,
    @SerializedName("crew") val crews : List<Crew>
)

