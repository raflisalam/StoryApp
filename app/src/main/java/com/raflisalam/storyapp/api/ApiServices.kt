package com.raflisalam.storyapp.api

import com.raflisalam.storyapp.model.auth.login.LoginResponse
import com.raflisalam.storyapp.model.auth.login.LoginUser
import com.raflisalam.storyapp.model.auth.register.RegisterResponse
import com.raflisalam.storyapp.model.auth.register.RegisterUser
import com.raflisalam.storyapp.model.stories.get.StoriesResponse
import com.raflisalam.storyapp.model.stories.post.PostStoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiServices {

    @Headers("Content-Type:application/json")
    @POST("register")
    suspend fun registerUser(
        @Body register: RegisterUser
    ): Response<RegisterResponse>

    @Headers("Content-Type:application/json")
    @POST("login")
    fun loginUser(
        @Body loginUser: LoginUser
    ): Call<LoginResponse>

    @Multipart
    @POST("stories")
    suspend fun postStories(
        @Header("Authorization") authToken: String,
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Response<PostStoriesResponse>

    @GET("stories")
    suspend fun storiesUser(
        @Header("Authorization") authToken: String
    ): Response<StoriesResponse>

}