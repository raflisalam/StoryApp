package com.raflisalam.storyapp.viewmodel.post.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raflisalam.storyapp.data.repository.Repository
import com.raflisalam.storyapp.model.auth.AuthError
import com.raflisalam.storyapp.model.auth.register.RegisterUser
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: Repository): ViewModel() {

    private val _error: MutableLiveData<Boolean> = MutableLiveData()
    val error: LiveData<Boolean> = _error

    private val _message: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String> = _message

    fun registerUser(register: RegisterUser) {
        viewModelScope.launch {
            try {
                repository.registerUser(register)
                _error.value = true
            } catch (error: AuthError) {
                _message.value = error.message
                _error.value = false
            }
        }
    }

}