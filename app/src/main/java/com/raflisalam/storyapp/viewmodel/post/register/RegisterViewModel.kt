package com.raflisalam.storyapp.viewmodel.post.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raflisalam.storyapp.model.RegisterUser
import com.raflisalam.storyapp.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class RegisterViewModel(private val repository: Repository): ViewModel() {

    var registerResponse: MutableLiveData<Response<RegisterUser>> = MutableLiveData()

    fun registerUser(register: RegisterUser) {
        viewModelScope.launch {
            val response = repository.registerUser(register)
            registerResponse.value = response
        }
    }
}