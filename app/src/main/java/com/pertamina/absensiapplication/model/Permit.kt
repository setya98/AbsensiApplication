package com.pertamina.absensiapplication.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Permit(
        @SerializedName("title")
        val title: String = "",
        @SerializedName("employeeNumber")
        val employeeNumber: String = "",
        @SerializedName("from")
        val from: String = "",
        @SerializedName("to")
        val to: String = "",
        @SerializedName("leaveDuration")
        val leaveDuration: Long = 0,
        @SerializedName("dateTo")
        val dateTo: String = "",
        @SerializedName("dateBack")
        val dateBack: String = "",
        @SerializedName("dateIn")
        val dateIn: String = "",
        @SerializedName("cost")
        val cost: String = "",
        @SerializedName("drive")
        val drive: String = "",
        @SerializedName("status")
        val status: StatusPermit = StatusPermit(),
        @SerializedName("userId")
        val userId: String= "",
        @SerializedName("senior")
        val senior: String="",
        @SerializedName("operationHead")
        val operationHead: String="",
        @SerializedName("profileImage")
        val profileImage: String="",
        @SerializedName("applicantName")
        val applicantName: String="",
        @SerializedName("type")
        val type: List<String> = ArrayList()
) : Parcelable