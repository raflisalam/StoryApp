package com.raflisalam.storyapp.model.login

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("loginResult")
    val authentication: LoginResult? = LoginResult()
)