package com.pertamina.absensiapplication.model

import com.google.gson.annotations.SerializedName

data class ResultPermit(
    @SerializedName("permits") val permits: List<Permit>
)