package com.raflisalam.storyapp.model.auth.login

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResult (
    val userId: String? = null,
    val name: String? = null,
    val token: String? = null
): Parcelable