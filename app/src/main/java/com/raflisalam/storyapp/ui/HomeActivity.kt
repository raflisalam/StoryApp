package com.raflisalam.storyapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.raflisalam.storyapp.databinding.ActivityHomeBinding
import com.raflisalam.storyapp.model.login.LoginResult
import com.raflisalam.storyapp.repository.Repository
import com.raflisalam.storyapp.viewmodel.post.register.RegisterViewModel
import com.raflisalam.storyapp.viewmodel.post.register.RegisterViewModelFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getParcelableData()
    }

    private fun getParcelableData() {
        val data: LoginResult = intent.getParcelableExtra<LoginResult>(LOGIN_RESULT) as LoginResult
        binding.tesResult.text = data.name
        Log.d("isian", data.name.toString())
    }

    companion object {
        const val LOGIN_RESULT = "response"
    }

}