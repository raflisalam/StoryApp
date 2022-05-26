package com.raflisalam.storyapp.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.raflisalam.storyapp.R
import com.raflisalam.storyapp.databinding.ActivityHomeBinding
import com.raflisalam.storyapp.pref.UserSession
import com.raflisalam.storyapp.ui.auth.LoginActivity
import com.raflisalam.storyapp.ui.stories.PostStoriesActivity
import com.raflisalam.storyapp.viewmodel.session.SessionFactoryViewModel
import com.raflisalam.storyapp.viewmodel.session.SessionViewModel
import java.io.IOException

class HomeActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

    private lateinit var binding: ActivityHomeBinding
    private lateinit var session: SessionViewModel
    private lateinit var userSession: UserSession
    private lateinit var uri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupActionBar()
        setupBottomNavigation()
    }

    private fun setupViewModel() {
        userSession = UserSession.newInstance(this.dataStore)
        session = ViewModelProvider(this, SessionFactoryViewModel(userSession))[SessionViewModel::class.java]
    }

    private fun setupBottomNavigation() {
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_maps, R.id.navigation_paging
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun setupActionBar() {
        val actionBar = supportActionBar
        actionBar?.show()
        actionBar?.title = getString(R.string.home_activity)
        actionBar?.title = (Html.fromHtml("<font color=\"black\">" + getString(R.string.home_activity) + "</font" ))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_add_stories, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.button_add -> {
                ImagePicker.Companion.with(this)
                    .compress(1024)
                    .maxResultSize(1080,1080)
                    .start(2)
            }
            R.id.button_logout -> {
                logout()
                finish()
            }
            R.id.button_settings -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            uri = data.data!!
            try {
                val intent = Intent(this, PostStoriesActivity::class.java)
                intent.putExtra(PostStoriesActivity.URI_IMAGE, uri.toString())
                startActivity(intent)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun logout() {
        resetSessionUser()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun resetSessionUser() {
        session.setSessionLogin("", "",false)
        val logoutPref = getSharedPreferences(NAME_KEY_SESSION, MODE_PRIVATE)
        val session: SharedPreferences.Editor = logoutPref.edit()
        session.putString(KEY_SESSION, "false")
        session.apply()

        val tokenPref = getSharedPreferences(NAME_KEY_TOKEN, MODE_PRIVATE)
        val token: SharedPreferences.Editor = tokenPref.edit()
        token.putString(KEY_TOKEN, "")
        token.apply()
    }

    companion object {
        private const val NAME_KEY_TOKEN = "user_token"
        private const val KEY_TOKEN = "token"

        private const val NAME_KEY_SESSION = "user_session"
        private const val KEY_SESSION = "session"
    }

}