package com.raflisalam.storyapp.viewmodel.get

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.raflisalam.storyapp.repository.Repository

class StoriesFactoryViewModel(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StoriesViewModel(repository) as T
    }

}