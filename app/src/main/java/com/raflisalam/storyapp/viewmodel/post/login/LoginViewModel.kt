package com.raflisalam.storyapp.viewmodel.post.login

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.raflisalam.storyapp.api.ApiClient
import com.raflisalam.storyapp.model.auth.login.LoginResponse
import com.raflisalam.storyapp.model.auth.login.LoginResult
import com.raflisalam.storyapp.model.auth.login.LoginUser
import com.raflisalam.storyapp.model.auth.login.ResponseError
import com.raflisalam.storyapp.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class LoginViewModel(private val repository: Repository): ViewModel() {

//    val loginResponse: LiveData<LoginResponse> = _loginResponse


//    private var _checkError: MutableLiveData<Boolean?> = MutableLiveData()
//    val checkError: LiveData<Boolean?> = _checkError

    /*private var _errorMessage: MutableLiveData<String?> = MutableLiveData()
    val errorMessage: LiveData<String?> = _errorMessage

*/


    private val loginResult: MutableLiveData<LoginResult> = MutableLiveData()
    private val _error = MutableLiveData<Boolean>()

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun loginUser(loginUser: LoginUser) {
        ApiClient.instance.loginUser(loginUser).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    _error.postValue(response.body()?.error)
                    _message.postValue(response.body()?.message)
                    loginResult.postValue(response.body()?.authentication!!)
                } else {
                    _error.postValue(response.body()?.error)
                    _message.postValue(response.body()?.message)
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









/*
    fun loginUser(loginUser: LoginUser) {
        viewModelScope.launch(Dispatchers.Main) {
            val response = repository.loginUser(loginUser)
            loginResponse.value = response
            loginResult.value = response.body()?.authentication!!
        }
    }
*/




/*
    fun loginUser(loginUser: LoginUser) {
        repository.loginUser(loginUser).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    _loginResponse.value = response.body()
                    loginResult.value = response.body()?.authentication!!
                } else if (!response.isSuccessful) {
                    Log.d("thisGagal", response.message())
                    _errorMessage.value = response.body()?.message
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                t.message?.let { Log.d("failure", it) }
            }
        })
    }
*/

/*
    fun loginUser(loginUser: LoginUser) {
        viewModelScope.launch {
            val response = repository.loginUser(loginUser)
            if (response.isSuccessful) {
                loginResponse.value = response.body()
                loginResult.value = response.body()?.authentication!!
            } else {
                try {
                    val data = response.message()
                    _errorMessage.postValue(data)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
*/


//          /*  if (response.isSuccessful) {
//                _checkError.value = response.body()?.error
//                loginResult.value = response.body()?.authentication!!
//            } else {
//                _checkError.value = response.body()?.error
//                _errorMessage.value = response.body()?.message
//            }*/
//        }
//    }

//    fun getLoginResponse(): LiveData<LoginResponse> {
//        return loginResponse
//    }
