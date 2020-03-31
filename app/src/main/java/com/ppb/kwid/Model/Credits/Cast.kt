package com.ppb.kwid.Model.Credits

import com.google.gson.annotations.SerializedName

data class Cast(
    @SerializedName("character") val character: String,
    @SerializedName("name") val name: String,
    @SerializedName("profile_path") val profilePath: String
)