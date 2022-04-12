package com.raflisalam.storyapp.model.stories.post

import com.google.gson.annotations.SerializedName

data class PostStoriesResponse(
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String
)
