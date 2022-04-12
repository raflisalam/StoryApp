package com.raflisalam.storyapp.viewmodel.post.stories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raflisalam.storyapp.model.stories.post.PostStoriesResponse
import com.raflisalam.storyapp.repository.Repository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class PostStoriesViewModel(private val repository: Repository): ViewModel() {

    var postResponse: MutableLiveData<Response<PostStoriesResponse>> = MutableLiveData()

    fun postStories(authToken: String, multipartBody: MultipartBody.Part, description: RequestBody) {
        viewModelScope.launch {
            val response = repository.postStories(authToken, multipartBody, description)
            postResponse.value = response
        }
    }
}