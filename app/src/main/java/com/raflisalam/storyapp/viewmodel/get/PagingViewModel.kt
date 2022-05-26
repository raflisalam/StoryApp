package com.raflisalam.storyapp.viewmodel.get

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.raflisalam.storyapp.data.Injection
import com.raflisalam.storyapp.data.repository.PagingRepository
import com.raflisalam.storyapp.model.stories.get.StoriesUsers

class PagingViewModel(pagingRepository: PagingRepository): ViewModel() {

    val getStories: LiveData<PagingData<StoriesUsers>> = pagingRepository.getStories().cachedIn(viewModelScope)

}

class PagingViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PagingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PagingViewModel(Injection.providePagingRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}