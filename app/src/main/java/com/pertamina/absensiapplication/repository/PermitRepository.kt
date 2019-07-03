package com.pertamina.absensiapplication.repository

import android.util.Log
import com.pertamina.absensiapplication.api.PermitApi
import com.pertamina.absensiapplication.model.Permit
import com.pertamina.absensiapplication.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PermitRepository(private val service: PermitApi) {
    suspend fun getPermitsByUid(userId: String): MutableList<Permit> {
        val permits = mutableListOf<Permit>()
        val request = service.getPermitsByUid(userId).await()
        request.permits.forEach {
            permits.add(it)
        }
        return permits
    }

    suspend fun getUser(uid: String) = service.getUser(uid).await()

    fun createPermit(userId: String, permit: Permit) {
        service.createPermit(userId, permit).enqueue(object : Callback<Permit> {
            override fun onFailure(call: Call<Permit>?, t: Throwable?) {
                Log.e("retrofit", "call failed")
            }

            override fun onResponse(call: Call<Permit>?, response: Response<Permit>?) {
            }

        })
    }

    suspend fun createAccount(email: String, password: String) =
        service.createAccount(email, password).await()

    suspend fun createUserData(uid: String, user: User) = service.createUserData(uid, user).await()

    suspend fun getUsers(): MutableList<User> {
        val users = mutableListOf<User>()
        val request = service.getUsers().await()
        request.users.forEach {
            users.add(it)
        }
        return users
    }

    suspend fun getOH(): MutableList<User> {
        val users = mutableListOf<User>()
        val request = service.getOH().await()
        request.users.forEach {
            users.add(it)
        }
        return users
    }

    suspend fun getSenior(): MutableList<User> {
        val users = mutableListOf<User>()
        val request = service.getSenior().await()
        request.users.forEach {
            users.add(it)
        }
        return users
    }
}