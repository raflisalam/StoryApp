package com.raflisalam.storyapp.viewmodel.post.stories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.raflisalam.storyapp.repository.Repository

class PostStoriesViewModelFactory(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PostStoriesViewModel(repository) as T
    }

}