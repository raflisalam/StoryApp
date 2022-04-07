package com.raflisalam.storyapp.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.raflisalam.storyapp.R
import com.raflisalam.storyapp.databinding.ActivityMainBinding
import com.raflisalam.storyapp.model.RegisterUser
import com.raflisalam.storyapp.repository.Repository
import com.raflisalam.storyapp.viewmodel.post.register.RegisterViewModel
import com.raflisalam.storyapp.viewmodel.post.register.RegisterViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFormRegister()
        setupViewModel()
    }

   private fun setupViewModel() {
        val repository = Repository()
        val viewModelFactory = RegisterViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)
    }

    private fun setupFormRegister() {
        binding.apply {
            val name = inputName.text
            val email = inputEmail.text
            val password = inputPassword.text
            setButtonRegister(name, email, password)
        }
    }

    private fun setButtonRegister(name: Editable?, email: Editable?, password: Editable?) {
        binding.apply {
            btnRegister.setOnClickListener {
                when {
                    TextUtils.isEmpty(inputName.toString()) -> {
                        inputName.error = getString(R.string.text_form_error)
                    }
                    TextUtils.isEmpty(inputEmail.toString()) -> {
                        inputEmail.error = getString(R.string.text_form_error)
                    }
                    TextUtils.isEmpty(inputPassword.toString()) -> {
                        inputPassword.error = getString(R.string.text_form_error)
                    }
                    else -> {
                        registerUser(name.toString(), email.toString(), password.toString())
                    }
                }
            }
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        val post = RegisterUser(name, email, password)
        Log.d("inputanRegister", post.toString())
        viewModel.registerUser(post)
        viewModel.registerResponse.observe(this, Observer {
            if (it.isSuccessful) {
                startActivity(Intent(this, LoginActivity::class.java))
                Toast.makeText(this, it.message(), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, it.message(), Toast.LENGTH_SHORT).show()
                Log.d("registerError", it.code().toString())
                Log.d("registerError", it.message().toString())
            }
        })
    }
}