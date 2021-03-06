package com.raflisalam.storyapp.viewmodel.session

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.raflisalam.storyapp.pref.UserSession
import kotlinx.coroutines.launch

class SessionViewModel(private val preferences: UserSession) : ViewModel() {

    fun setSessionLogin(name: String, token: String, session: Boolean) {
        viewModelScope.launch {
            preferences.setSessionUser(name, token, session)
        }
    }

    fun getUserSession(): LiveData<Boolean> {
        return preferences.getUserSession().asLiveData()
    }

}