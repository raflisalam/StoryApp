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
import com.raflisalam.storyapp.model.auth.login.LoginUser
import com.raflisalam.storyapp.pref.UserSession
import com.raflisalam.storyapp.repository.Repository
import com.raflisalam.storyapp.ui.costumview.button.ButtonProgress
import com.raflisalam.storyapp.ui.home.HomeActivity
import com.raflisalam.storyapp.viewmodel.post.login.LoginViewModel
import com.raflisalam.storyapp.viewmodel.session.SessionFactoryViewModel
import com.raflisalam.storyapp.viewmodel.session.SessionViewModel
import java.util.concurrent.Executor
import java.util.concurrent.Executors

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
        saveSession()
        setupActionBar()
    }

    private fun setupActionBar() {
        val actionBar = supportActionBar
        actionBar?.hide()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
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
        val button = ButtonProgress(this, btnLogin)
        btnLogin.setOnClickListener {
            button.isPressed()
            binding.apply {
                when {
                    TextUtils.isEmpty(inputEmail.toString()) -> {
                        inputEmail.error = getString(R.string.text_form_error)
                    }
                    TextUtils.isEmpty(inputPassword.toString()) -> {
                        inputPassword.error = getString(R.string.text_form_error)
                    }
                    else -> {
                        loginUser(email.toString(), password.toString())
                        loadingProgress()
                    }
                }
            }
        }
        binding.btnBelumDaftar.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun loginUser(email: String, password: String) {
        val post = LoginUser(email, password)
        viewModel.loginUser(post)
    }

    private fun loadingProgress() {
        val button = ButtonProgress(this, btnLogin)
        viewModel.error().observe(this) { error ->
            if (error == false) {
                loginSuccess = true
                storedDataUser(loginSuccess)
                Handler().postDelayed({
                    button.afterProgress()
                    startActivity()
                }, 3000)
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                Handler().postDelayed({
                    button.afterProgress()
                }, 3000)
            }
        }
    }


    private fun storedDataUser(loginSession: Boolean) {
        viewModel.getLoginResult().observe(this) { data ->
            session.setSessionLogin(data.token!!, loginSession)
        }
    }

    private fun startActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
    }

    private fun saveSession() {
        session.getUserSession().observe(this) { loginSuccess ->
            Log.d("checkResultSession", loginSuccess.toString())
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
        checkSession()
    }

    private fun checkSession() {
        val getLoginPref = getSharedPreferences(NAME_KEY_SESSION, MODE_PRIVATE)
        val check = getLoginPref.getString(KEY_SESSION, "")
        if (check.equals("true")) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        } else if (check.equals("false")){
            Toast.makeText(this, getString(R.string.must_loginfirst), Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val NAME_KEY_SESSION = "user_session"
        private const val KEY_SESSION = "session"
    }
}