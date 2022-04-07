package com.raflisalam.storyapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.raflisalam.storyapp.api.ApiClient
import com.raflisalam.storyapp.model.login.LoginUser
import com.raflisalam.storyapp.model.RegisterUser
import com.raflisalam.storyapp.model.login.LoginResponse
import com.raflisalam.storyapp.model.login.LoginResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository {

    suspend fun registerUser(register: RegisterUser): Response<RegisterUser> {
        return ApiClient.instance.registerUser(register)
    }

    suspend fun loginUser(login: LoginUser): Response<LoginResponse> {
        return ApiClient.instance.loginUser2(login)
    }

}