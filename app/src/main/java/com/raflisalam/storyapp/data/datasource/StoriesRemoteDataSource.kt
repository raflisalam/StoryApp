package com.raflisalam.storyapp.data.datasource

import com.raflisalam.storyapp.api.ApiServices
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoriesRemoteDataSource(private val apiServices: ApiServices) {

    suspend fun storiesMaps(
        authToken: String,
        location: Int
    ) = apiServices.storiesMaps("Bearer $authToken", location)

    suspend fun postStories(
        authToken: String,
        file: MultipartBody.Part,
        description: RequestBody
    ) = apiServices.postStories("Bearer $authToken", file, description)

}