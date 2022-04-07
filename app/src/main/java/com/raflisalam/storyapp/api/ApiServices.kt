package com.raflisalam.storyapp.api

import com.raflisalam.storyapp.model.RegisterUser
import com.raflisalam.storyapp.model.login.LoginResponse
import com.raflisalam.storyapp.model.login.LoginUser
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiServices {

    @Headers("Content-Type:application/json")
    @POST("register")
    suspend fun registerUser(
        @Body register: RegisterUser
    ): Response<RegisterUser>

    @FormUrlEncoded
    @Headers("Content-Type:application/json")
    @POST("login")
    suspend fun loginUser(
        @Field("email") email : String,
        @Field("password") password : String
    ): Call<LoginResponse>

    @Headers("Content-Type:application/json")
    @POST("login")
    suspend fun loginUser2(
        @Body loginUser: LoginUser
    ): Response<LoginResponse>
}