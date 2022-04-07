package com.raflisalam.storyapp.viewmodel.post.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.raflisalam.storyapp.repository.Repository

class LoginViewModelFactory(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(repository) as T
    }
}