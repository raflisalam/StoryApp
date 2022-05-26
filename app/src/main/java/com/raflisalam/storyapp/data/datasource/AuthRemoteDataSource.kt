package com.raflisalam.storyapp.data.datasource

import com.raflisalam.storyapp.api.ApiServices
import com.raflisalam.storyapp.model.auth.login.LoginUser
import com.raflisalam.storyapp.model.auth.register.RegisterUser

class AuthRemoteDataSource(private val apiServices: ApiServices) {

    suspend fun registerUser(register: RegisterUser) = apiServices.registerUser(register)
}