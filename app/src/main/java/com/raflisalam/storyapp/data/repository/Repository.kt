package com.raflisalam.storyapp.data.repository

import android.content.Context
import com.raflisalam.storyapp.R
import com.raflisalam.storyapp.data.datasource.AuthRemoteDataSource
import com.raflisalam.storyapp.data.datasource.StoriesRemoteDataSource
import com.raflisalam.storyapp.model.auth.AuthError
import com.raflisalam.storyapp.model.auth.register.RegisterUser
import com.raflisalam.storyapp.model.stories.StoriesError
import com.raflisalam.storyapp.model.stories.get.StoriesUsers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody

open class Repository(
    private val context: Context,
    private val authDataSource: AuthRemoteDataSource,
    private val storiesDataSource: StoriesRemoteDataSource
    ) {

    suspend fun registerUser(register: RegisterUser) {
        try {
            val response = authDataSource.registerUser(register)
            if (!response.isSuccessful) {
                throw AuthError(
                    response.body()?.message ?: context.getString(R.string.fail_register)
                )
            }
        } catch (e: Throwable) {
            throw AuthError(e.message.toString())
        }
    }

    suspend fun storiesMaps(authToken: String): List<StoriesUsers> {
        val listStories = mutableListOf<StoriesUsers>()
        withContext(Dispatchers.IO) {
            try {
                val response = storiesDataSource.storiesMaps(authToken, 1)
                if (response.isSuccessful) {
                    response.body()?.listStories?.forEach { data ->
                        listStories.add(
                            StoriesUsers(
                                id = data.id,
                                photoUrl = data.photoUrl,
                                name = data.name,
                                description = data.description,
                                createdAt = data.createdAt,
                                lat = data.lat,
                                lon = data.lon
                            )
                        )
                    }
                }
            } catch (e: Throwable) {
                throw StoriesError(e.message.toString())
            }
        }
        return listStories
    }

    suspend fun postStories(authToken: String, multipart: MultipartBody.Part, description: RequestBody) {
        withContext(Dispatchers.IO) {
            try {
                val response = storiesDataSource.postStories(authToken, multipart, description)
                if (!response.isSuccessful) {
                    throw StoriesError(response.message())
                }
            } catch (e: Throwable) {
                throw StoriesError(e.message.toString())
            }
        }
    }
}