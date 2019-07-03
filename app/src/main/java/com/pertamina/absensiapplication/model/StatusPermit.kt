package com.pertamina.absensiapplication.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StatusPermit(
    @SerializedName("isNegotiate")
    val isNegotiate: Boolean = false,
    @SerializedName("confirmBySenior")
    val confirmBySenior: Boolean = false,
    @SerializedName("confirmByOH")
    val confirmByOH: Boolean = false,
    @SerializedName("isComplete")
    val isComplete: Boolean = false,
    @SerializedName("isRequest")
    val isRequest: Boolean = false
    ) : Parcelable