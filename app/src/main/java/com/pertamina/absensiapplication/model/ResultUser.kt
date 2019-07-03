package com.pertamina.absensiapplication.model

import com.google.gson.annotations.SerializedName

data class ResultUser(
    @SerializedName("users") val users: List<User>
)