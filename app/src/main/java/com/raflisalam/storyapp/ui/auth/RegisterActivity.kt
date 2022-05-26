package com.raflisalam.storyapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.raflisalam.storyapp.R
import com.raflisalam.storyapp.databinding.ActivityRegisterBinding
import com.raflisalam.storyapp.model.auth.register.RegisterUser
import com.raflisalam.storyapp.ui.costumview.button.ButtonProgress
import com.raflisalam.storyapp.viewmodel.post.register.RegisterViewModel
import com.raflisalam.storyapp.viewmodel.post.register.RegisterViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory.getInstance(this)
    }

    private lateinit var btnRegister: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnRegister = findViewById(R.id.btnRegister)

        setupFormRegister()
        setupActionBar()
    }

    private fun setupActionBar() {
        val actionBar = supportActionBar
        actionBar?.hide()
    }

/*
    private fun setupViewModel() {
        val repository = Repository(this, )
        val viewModelFactory = RegisterViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[RegisterViewModel::class.java]
    }
*/

    private fun setupFormRegister() {
        binding.apply {
            val name = inputName.text
            val email = inputEmail.text
            val password = inputPassword.text
            setButtonRegister(name, email, password)
        }
    }

    private fun setButtonRegister(name: Editable?, email: Editable?, password: Editable?) {
        btnRegister.setOnClickListener {
            binding.apply {
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
                        loadingProgress()
                    }
                }
            }
        }
        binding.btnSudahDaftar.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        val post = RegisterUser(name, email, password)
        viewModel.registerUser(post)
    }

    private fun loadingProgress() {
        val button = ButtonProgress(this, btnRegister)
        button.isPressed()
        viewModel.error.observe(this) { success ->
            if (success) {
                Handler().postDelayed({
                    button.afterProgress()
                    startActivity()
                }, 3000)
            }
        }
        viewModel.message.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun startActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        Toast.makeText(this, getString(R.string.register_success), Toast.LENGTH_SHORT).show()
    }
}