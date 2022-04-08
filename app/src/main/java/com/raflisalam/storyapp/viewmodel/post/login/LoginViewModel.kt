package com.raflisalam.storyapp.viewmodel.post.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raflisalam.storyapp.model.login.LoginResponse
import com.raflisalam.storyapp.model.login.LoginResult
import com.raflisalam.storyapp.model.login.LoginUser
import com.raflisalam.storyapp.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel(private val repository: Repository): ViewModel() {

    var loginResponse: MutableLiveData<Response<LoginResponse>> = MutableLiveData()
    private val loginResult: MutableLiveData<LoginResult> = MutableLiveData()

    fun loginUser(loginUser: LoginUser) {
        viewModelScope.launch(Dispatchers.Main) {
            val response = repository.loginUser(loginUser)
            loginResponse.value = response
            loginResult.value = response.body()?.authentication
            Log.d("resultResponse", loginResult.value.toString())
        }
    }

    fun getLoginResult(): LiveData<LoginResult> {
        return loginResult
    }

}