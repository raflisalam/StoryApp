package com.raflisalam.storyapp.viewmodel.get

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raflisalam.storyapp.data.repository.StoriesRepo
import com.raflisalam.storyapp.model.stories.get.StoriesResponse
import com.raflisalam.storyapp.model.stories.get.StoriesUsers
import kotlinx.coroutines.launch
import retrofit2.Response

class StoriesViewModel(private val repository: StoriesRepo): ViewModel() {

    private val _storiesResponse: MutableLiveData<Response<StoriesResponse>> = MutableLiveData()
    val storiesResponse: LiveData<Response<StoriesResponse>> = _storiesResponse

    private val _listStories: MutableLiveData<List<StoriesUsers>> = MutableLiveData()
    val listStories: LiveData<List<StoriesUsers>> = _listStories

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading

    fun getStories(authToken: String) {
        _loading.value = true
        viewModelScope.launch {
            val response = repository.storiesUsers(authToken)
            if (response.isSuccessful) {
                _loading.value = false
                _listStories.value = response.body()?.listStories
                _storiesResponse.value = response
            }
        }
    }
}