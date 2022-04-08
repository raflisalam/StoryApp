package com.raflisalam.storyapp.repository

import com.raflisalam.storyapp.api.ApiClient
import com.raflisalam.storyapp.model.RegisterUser
import com.raflisalam.storyapp.model.login.LoginResponse
import com.raflisalam.storyapp.model.login.LoginUser
import retrofit2.Response

class Repository {

    suspend fun registerUser(register: RegisterUser): Response<RegisterUser> {
        return ApiClient.instance.registerUser(register)
    }

    suspend fun loginUser(login: LoginUser): Response<LoginResponse> {
        return ApiClient.instance.loginUser2(login)
    }

}