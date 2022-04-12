package com.raflisalam.storyapp.viewmodel.post.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.raflisalam.storyapp.api.ApiClient
import com.raflisalam.storyapp.model.auth.login.LoginResponse
import com.raflisalam.storyapp.model.auth.login.LoginResult
import com.raflisalam.storyapp.model.auth.login.LoginUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val loginResult: MutableLiveData<LoginResult> = MutableLiveData()
    private val _error = MutableLiveData<Boolean>()

    fun loginUser(loginUser: LoginUser) {
        ApiClient.instance.loginUser(loginUser).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    _error.postValue(response.body()?.error)
                    loginResult.postValue(response.body()?.authentication!!)
                } else {
                    _error.postValue(response.body()?.error)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                t.message?.let { Log.d("failure", it) }
            }

        })
    }

    fun error(): LiveData<Boolean> {
        return _error
    }

    fun getLoginResult(): LiveData<LoginResult> {
        return loginResult
    }

}