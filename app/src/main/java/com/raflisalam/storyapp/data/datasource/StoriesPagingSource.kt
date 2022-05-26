package com.raflisalam.storyapp.data.datasource

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.raflisalam.storyapp.api.ApiServices
import com.raflisalam.storyapp.model.stories.get.StoriesUsers

class StoriesPagingSource(private val apiServices: ApiServices, context: Context) : PagingSource<Int, StoriesUsers>() {
    private var tokenPref: SharedPreferences = context.getSharedPreferences(NAME_KEY_TOKEN, MODE_PRIVATE)
    private val token = tokenPref.getString(KEY_TOKEN, "")

    override fun getRefreshKey(state: PagingState<Int, StoriesUsers>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoriesUsers> {

        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiServices.pagingStories("Bearer $token", page, params.loadSize)

            LoadResult.Page(
                data = responseData.listStories,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.listStories.isNullOrEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }


    private companion object {
        const val INITIAL_PAGE_INDEX = 1

        private const val NAME_KEY_TOKEN = "user_token"
        private const val KEY_TOKEN = "token"
    }
}