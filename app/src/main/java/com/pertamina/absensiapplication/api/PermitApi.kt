package com.pertamina.absensiapplication.api

import com.pertamina.absensiapplication.model.Permit
import com.pertamina.absensiapplication.model.ResultPermit
import com.pertamina.absensiapplication.model.ResultUser
import com.pertamina.absensiapplication.model.User
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PermitApi {
    @GET("permits/{id}")
    fun getPermitsByUid(
        @Path("id") userId: String
    ): Deferred<ResultPermit>

    @GET("users")
    fun getUsers(): Deferred<ResultUser>

    @GET("user/{id}")
    fun getUser(
        @Path("id") userId: String
    ): Deferred<User>

    @GET("userOH")
    fun getOH(): Deferred<ResultUser>

    @GET("userSenior")
    fun getSenior(): Deferred<ResultUser>

    @POST("permit/{id}")
    fun createPermit(
        @Path("id") userId: String,
        @Body permit: Permit
    ): Call<Permit>

    @GET("/auth/{email}/{password}")
    fun createAccount(
        @Path("email") email: String,
        @Path("password") password: String
    ): Deferred<String>

    @POST("user/{id}")
    fun createUserData(
        @Path("id") userId: String,
        @Body user: User
    ): Deferred<User>
}