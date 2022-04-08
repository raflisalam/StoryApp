package com.raflisalam.storyapp.viewmodel.session

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.raflisalam.storyapp.pref.UserSession
import kotlinx.coroutines.launch

class SessionViewModel(private val preferences: UserSession) : ViewModel() {

    fun setSessionLogin(userId: String, name: String, token: String) {
        viewModelScope.launch {
            preferences.setSessionUser(userId, name, token)
        }
    }

    fun getUserId(): LiveData<String> {
        return preferences.getUserId().asLiveData()
    }

}