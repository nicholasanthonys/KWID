package com.ppb.kwid.Model.Credits

import com.google.gson.annotations.SerializedName
import com.ppb.kwid.Model.Credits.Cast
import com.ppb.kwid.Model.Credits.Crew

class GetCreditsResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("cast") val casts: List<Cast>,
    @SerializedName("crew") val crews : List<Crew>
)

