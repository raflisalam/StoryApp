package com.raflisalam.storyapp.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.raflisalam.storyapp.api.ApiServices
import com.raflisalam.storyapp.data.datasource.StoriesPagingSource
import com.raflisalam.storyapp.model.stories.get.StoriesUsers
import kotlinx.coroutines.flow.Flow

class PagingRepository(private val apiServices: ApiServices, private val context: Context) {

    fun getStories(): LiveData<PagingData<StoriesUsers>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoriesPagingSource(apiServices, context)
            }
        ).liveData
    }
}