package com.raflisalam.storyapp.data

import android.content.Context
import com.raflisalam.storyapp.api.ApiClient
import com.raflisalam.storyapp.data.repository.PagingRepository
import com.raflisalam.storyapp.data.datasource.AuthRemoteDataSource
import com.raflisalam.storyapp.data.datasource.StoriesRemoteDataSource
import com.raflisalam.storyapp.data.repository.Repository

object Injection {
    fun providePagingRepository(context: Context): PagingRepository {
        val apiServices = ApiClient.instance
        return PagingRepository(apiServices, context)
    }

    fun provideRepository(context: Context): Repository {
        val apiService = ApiClient.instance
        val authDataSource = AuthRemoteDataSource(apiService)
        val storiesDataSource = StoriesRemoteDataSource(apiService)
        return Repository(context, authDataSource, storiesDataSource)
    }


}