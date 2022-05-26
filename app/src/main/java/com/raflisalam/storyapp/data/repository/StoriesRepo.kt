package com.raflisalam.storyapp.data.repository

import com.raflisalam.storyapp.api.ApiClient
import com.raflisalam.storyapp.model.stories.get.StoriesResponse
import retrofit2.Response

class StoriesRepo {
    suspend fun storiesUsers(authToken: String): Response<StoriesResponse> {
        return ApiClient.instance.storiesUser(authToken)
    }
}