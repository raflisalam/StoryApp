package com.raflisalam.storyapp.model.auth.register

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String
)
