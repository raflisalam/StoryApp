package com.raflisalam.storyapp.viewmodel.get

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.raflisalam.storyapp.data.repository.StoriesRepo

class StoriesFactoryViewModel(private val repository: StoriesRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StoriesViewModel(repository) as T
    }

}