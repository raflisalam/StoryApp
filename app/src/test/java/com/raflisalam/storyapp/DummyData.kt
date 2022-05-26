package com.raflisalam.storyapp

import androidx.paging.PagingData
import com.raflisalam.storyapp.model.stories.get.StoriesUsers

object DummyData {
    fun generateStoriesItem(): List<StoriesUsers> {
        val listStories = mutableListOf<StoriesUsers>()
        repeat(10) {
            listStories.add(
                StoriesUsers(
                    photoUrl = "https://linkedin.com/raflisalam",
                    createdAt = "time-$it",
                    name = "fake name-$it",
                    description = "description-$it",
                    lon = it.toDouble(),
                    id = "user-rafli-$it",
                    lat = it.toDouble()
                )
            )
        }
        return listStories
    }

    fun generatePagingData(): PagingData<StoriesUsers> = PagingData.from(getListStoriesModel())

    fun getListStoriesModel(): List<StoriesUsers> {
        val stories = mutableListOf<StoriesUsers>()
        generateStoriesItem().forEach {
            stories.add(
                StoriesUsers(
                    photoUrl = it.photoUrl,
                    name = it.name,
                    description = it.description,
                    lon = it.lon,
                    id = it.id,
                    lat = it.lat
                )
            )
        }
        return stories
    }
}