package com.raflisalam.storyapp.viewmodel.post.stories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raflisalam.storyapp.data.repository.Repository
import com.raflisalam.storyapp.model.stories.StoriesError
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PostStoriesViewModel(private val repository: Repository): ViewModel() {

    private val _isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _message: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String> = _message

    fun postStories(authToken: String, file: MultipartBody.Part, description: RequestBody) {
        viewModelScope.launch {
            try {
                repository.postStories(authToken, file, description)
                _isSuccess.value = true
            } catch (e: StoriesError) {
                _message.value = e.message
                _isSuccess.value = false
            }
        }
    }
}