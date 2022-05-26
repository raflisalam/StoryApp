package com.raflisalam.storyapp.viewmodel.get.maps

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.raflisalam.storyapp.data.Injection
import com.raflisalam.storyapp.data.repository.Repository
import com.raflisalam.storyapp.viewmodel.get.StoriesFactoryViewModel

class MapsViewModelFactory(private val repository: Repository): ViewModelProvider.Factory {

    companion object {
        @Volatile
        private var instance: MapsViewModelFactory? = null
        fun getInstance(context: Context): MapsViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: MapsViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapsViewModel(repository) as T
    }
}