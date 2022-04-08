package com.raflisalam.storyapp.ui.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.raflisalam.storyapp.R
import com.raflisalam.storyapp.databinding.ActivityLoginBinding
import com.raflisalam.storyapp.model.login.LoginUser
import com.raflisalam.storyapp.pref.UserSession
import com.raflisalam.storyapp.repository.Repository
import com.raflisalam.storyapp.ui.HomeActivity
import com.raflisalam.storyapp.ui.costumview.button.ButtonProgress
import com.raflisalam.storyapp.viewmodel.post.login.LoginViewModel
import com.raflisalam.storyapp.viewmodel.post.login.LoginViewModelFactory
import com.raflisalam.storyapp.viewmodel.session.SessionFactoryViewModel
import com.raflisalam.storyapp.viewmodel.session.SessionViewModel

class LoginActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var session: SessionViewModel
    private lateinit var btnLogin: View
    private lateinit var userSession: UserSession
    private var loginSuccess: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnLogin = findViewById(R.id.btnLogin)

        setupFormLogin()
        setupViewModel()
        checkSession()
    }

    private fun setupViewModel() {
        val repository = Repository()
        val viewModelFactory = LoginViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
        userSession = UserSession.newInstance(dataStore)
        session = ViewModelProvider(this, SessionFactoryViewModel(userSession))[SessionViewModel::class.java]

    }

    private fun setupFormLogin() {
        binding.apply {
            val email = inputEmail.text
            val password = inputPassword.text
            setupButtonLogin(email, password)
        }
    }

    private fun setupButtonLogin(email: Editable?, password: Editable?) {
        btnLogin.setOnClickListener {
            binding.apply {
                when {
                    TextUtils.isEmpty(inputEmail.toString()) -> {
                        inputEmail.error = getString(R.string.text_form_error)
                    }
                    TextUtils.isEmpty(inputPassword.toString()) -> {
                        inputPassword.error = getString(R.string.text_form_error)
                    }
                    else -> {
                        loadingProgress()
                        loginUser(email.toString(), password.toString())
                    }
                }
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        val post = LoginUser("raflisalam.dokumen@gmail.com", "rafli12345")
        viewModel.loginUser(post)
    }

    private fun loadingProgress() {
        val button = ButtonProgress(this, btnLogin)
        button.isPressed()
        viewModel.loginResponse.observe(this) {
        if (it.isSuccessful) {
            loginSuccess = true
            saveSession(loginSuccess)
            storedDataUser()
            Handler().postDelayed({
                button.afterProgress()
                startActivity()
            }, 4000)
            Log.d("loginSuccess", it.code().toString())
            Log.d("loginSuccess", it.message().toString())
        } else {
            button.afterProgress()
            Toast.makeText(this, "Invalid email or password!", Toast.LENGTH_SHORT).show()
            Log.d("loginError", it.code().toString())
            Log.d("loginError", it.message().toString())
            }
        }
    }

    private fun storedDataUser() {
        viewModel.getLoginResult().observe(this) { data ->
            session.setSessionLogin(data.userId!!, data.name!!, data.token!!)
        }
    }

    private fun saveSession(loginSuccess: Boolean) {
        if (loginSuccess) {
            val loginPref = getSharedPreferences(NAME_KEY_SESSION, MODE_PRIVATE)
            val session: SharedPreferences.Editor = loginPref.edit()
            session.putString(KEY_SESSION, "true")
            session.apply()
        } else if (!loginSuccess) {
            val loginPref = getSharedPreferences(NAME_KEY_SESSION, MODE_PRIVATE)
            val session: SharedPreferences.Editor = loginPref.edit()
            session.putString(KEY_SESSION, "false")
            session.apply()
        }
    }

    private fun checkSession() {
        val getLoginPref = getSharedPreferences(NAME_KEY_SESSION, MODE_PRIVATE)
        val check = getLoginPref.getString(KEY_SESSION, "")
        if (check.equals("true")) {
            startActivity(Intent(this, HomeActivity::class.java))
        } else if (check.equals("false")){
            Toast.makeText(this, "Login first!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
    }

    companion object {
        private const val NAME_KEY_SESSION = "user_session"
        private const val KEY_SESSION = "session"
    }
}