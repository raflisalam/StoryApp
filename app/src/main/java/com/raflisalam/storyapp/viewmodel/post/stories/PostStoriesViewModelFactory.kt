package com.raflisalam.storyapp.viewmodel.post.stories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.raflisalam.storyapp.data.Injection
import com.raflisalam.storyapp.data.repository.Repository
import com.raflisalam.storyapp.viewmodel.get.StoriesFactoryViewModel

class PostStoriesViewModelFactory(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PostStoriesViewModel(repository) as T
    }

    companion object {
        @Volatile
        private var instance: PostStoriesViewModelFactory? = null
        fun getInstance(context: Context): PostStoriesViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: PostStoriesViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }

}