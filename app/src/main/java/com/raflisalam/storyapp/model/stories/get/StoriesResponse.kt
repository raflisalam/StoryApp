package com.raflisalam.storyapp.model.stories.get

import com.google.gson.annotations.SerializedName

data class StoriesResponse(
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String,
    @SerializedName("listStory")
    val listStories: List<StoriesUsers>
)
