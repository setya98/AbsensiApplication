package com.pertamina.absensiapplication.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val userId: String = "",
    val employeeNumber: String = "",
    val leaveBalance: Int = 0,
    val name: String = "",
    val position: String = "",
    val senior: String = "",
    val operationHead: String = "",
    val profileImage: String = "") : Parcelable