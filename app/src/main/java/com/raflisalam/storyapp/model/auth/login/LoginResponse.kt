package com.raflisalam.storyapp.model.auth.login

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String,
    @SerializedName("loginResult")
    val authentication: LoginResult? = LoginResult()
)