package com.raflisalam.storyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.raflisalam.storyapp.R
import com.raflisalam.storyapp.databinding.ActivityLoginBinding
import com.raflisalam.storyapp.model.login.LoginResult
import com.raflisalam.storyapp.model.login.LoginUser
import com.raflisalam.storyapp.repository.Repository
import com.raflisalam.storyapp.viewmodel.post.login.LoginViewModel
import com.raflisalam.storyapp.viewmodel.post.login.LoginViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFormLogin()
        setupViewModel()
    }

    private fun setupViewModel() {
        val repository = Repository()
        val viewModelFactory = LoginViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
    }

    private fun setupFormLogin() {
        binding.apply {
            val email = inputEmail.text
            val password = inputPassword.text
            setupButtonLogin(email, password)
        }
    }

    private fun setupButtonLogin(email: Editable?, password: Editable?) {
        binding.apply {
            btnLogin.setOnClickListener {
                when {
                    TextUtils.isEmpty(inputEmail.toString()) -> {
                        inputEmail.error = getString(R.string.text_form_error)
                    }
                    TextUtils.isEmpty(inputPassword.toString()) -> {
                        inputPassword.error = getString(R.string.text_form_error)
                    }
                    else -> {
                        loginUser(email.toString(), password.toString())
                    }
                }
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        val post = LoginUser("raflisalam.dokumen@gmail.com", "rafli12345")
        viewModel.loginUser(post)
        viewModel.loginResponse.observe(this, Observer {
            if (it.isSuccessful) {
                startActivity()
                Toast.makeText(this, "Login Successfully!", Toast.LENGTH_SHORT).show()
                Log.d("loginSuccess", it.code().toString())
                Log.d("loginSuccess", it.message().toString())
            } else {
                Toast.makeText(this, "Invalid email or password!", Toast.LENGTH_SHORT).show()
                Log.d("loginError", it.code().toString())
                Log.d("loginError", it.message().toString())
            }
        })
    }

    private fun startActivity() {
        viewModel.getLoginResult().observe(this) { data ->
            Log.d("data", data.toString())
            val dataResult = LoginResult(data.userId, data.name, data.token)
            Log.d("dataResult", dataResult.toString())
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra(HomeActivity.LOGIN_RESULT, dataResult)
            startActivity(intent)
        }
    }
}