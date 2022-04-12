package com.raflisalam.storyapp.viewmodel.get

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raflisalam.storyapp.model.stories.get.StoriesResponse
import com.raflisalam.storyapp.model.stories.get.StoriesUsers
import com.raflisalam.storyapp.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class StoriesViewModel(private val repository: Repository): ViewModel() {

    var storiesResponse: MutableLiveData<Response<StoriesResponse>> = MutableLiveData()
    private val listStories: MutableLiveData<List<StoriesUsers>> = MutableLiveData()

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun getStories(authToken: String) {
        _loading.value = true
        viewModelScope.launch {
            val response = repository.storiesUsers(authToken)
            if (response.isSuccessful) {
                _loading.value = false
                listStories.value = response.body()?.listStories
                storiesResponse.value = response
            }
            Log.d("resultStories", listStories.value.toString())
        }
    }

    fun getDataStories(): LiveData<List<StoriesUsers>> {
        return listStories
    }

}