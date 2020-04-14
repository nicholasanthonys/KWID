package com.ppb.kwid.Model.Schedule

import com.google.gson.annotations.SerializedName
import java.util.*

data class DatePicker(
    @SerializedName("date") var date: String,
    @SerializedName("day") var day: String,
    @SerializedName("is_enabled") var is_enabled: Boolean,
    @SerializedName("is_selected") var is_selected: Boolean,
    @SerializedName("calendar") var calendar: String
)