package com.raflisalam.storyapp.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.raflisalam.storyapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Logout()
    }

    private fun Logout() {
        binding.btnLogout.setOnClickListener {
            val logoutPref = getSharedPreferences(NAME_KEY_SESSION, MODE_PRIVATE)
            val session: SharedPreferences.Editor = logoutPref.edit()
            session.putString(KEY_SESSION, "false")
            session.apply()
            finish()
        }
    }

    companion object {
        private const val NAME_KEY_SESSION = "user_session"
        private const val KEY_SESSION = "session"
    }


}