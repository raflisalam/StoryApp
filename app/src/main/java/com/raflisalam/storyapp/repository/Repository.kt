package com.raflisalam.storyapp.repository

import com.raflisalam.storyapp.api.ApiClient
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

class Repository {

    suspend fun registerUser(register: RegisterUser): Response<RegisterResponse> {
        return ApiClient.instance.registerUser(register)
    }

/*
    suspend fun loginUser(login: LoginUser): Response<LoginResponse> {
        return ApiClient.instance.loginUser(login)
    }
*/

    suspend fun postStories(authToken: String, multipart: MultipartBody.Part, description: RequestBody): Response<PostStoriesResponse> {
        return ApiClient.instance.postStories(authToken, multipart, description)
    }

    suspend fun storiesUsers(authToken: String): Response<StoriesResponse> {
        return ApiClient.instance.storiesUser(authToken)
    }

}