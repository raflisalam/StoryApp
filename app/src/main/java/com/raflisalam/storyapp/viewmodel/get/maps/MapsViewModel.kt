package com.raflisalam.storyapp.viewmodel.get.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raflisalam.storyapp.data.repository.Repository
import com.raflisalam.storyapp.model.stories.StoriesError
import com.raflisalam.storyapp.model.stories.get.StoriesUsers
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: Repository): ViewModel() {

    private val _listMaps: MutableLiveData<List<StoriesUsers>> = MutableLiveData()
    val listMaps: LiveData<List<StoriesUsers>> = _listMaps

    private val _message: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String> = _message

    fun getStoriesMap(authToken: String) {
        viewModelScope.launch {
            try {
                val response = repository.storiesMaps(authToken)
                _listMaps.value = response
            } catch (e: StoriesError) {
                _message.value = e.message.toString()
            }
        }
    }
}