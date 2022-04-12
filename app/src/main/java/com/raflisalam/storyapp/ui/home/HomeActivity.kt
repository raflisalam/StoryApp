package com.raflisalam.storyapp.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.raflisalam.storyapp.R
import com.raflisalam.storyapp.adapter.ListStoriesAdapter
import com.raflisalam.storyapp.databinding.ActivityHomeBinding
import com.raflisalam.storyapp.pref.UserSession
import com.raflisalam.storyapp.repository.Repository
import com.raflisalam.storyapp.ui.auth.LoginActivity
import com.raflisalam.storyapp.ui.stories.PostStoriesActivity
import com.raflisalam.storyapp.viewmodel.get.StoriesFactoryViewModel
import com.raflisalam.storyapp.viewmodel.get.StoriesViewModel
import com.raflisalam.storyapp.viewmodel.session.SessionFactoryViewModel
import com.raflisalam.storyapp.viewmodel.session.SessionViewModel
import java.io.IOException

class HomeActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

    private lateinit var binding: ActivityHomeBinding
    private lateinit var userSession: UserSession
    private lateinit var adapter: ListStoriesAdapter
    private lateinit var viewModel: StoriesViewModel
    private lateinit var session: SessionViewModel
    private lateinit var uri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupViewModel()
        setupAdapter()
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

    private fun setupActionBar() {
        val actionBar = supportActionBar
        actionBar?.title = getString(R.string.home_activity)
        actionBar?.title = (Html.fromHtml("<font color=\"black\">" + getString(R.string.home_activity) + "</font" ))
    }

    private fun setupViewModel() {
        val repository = Repository()
        val viewModelFactory = StoriesFactoryViewModel(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[StoriesViewModel::class.java]
        userSession = UserSession.newInstance(dataStore)
        session = ViewModelProvider(this, SessionFactoryViewModel(userSession))[SessionViewModel::class.java]

        viewModel.loading.observe(this){
            showLoading(it)
        }

        userSession.userToken.asLiveData().observe(this) { token ->
            token?.let {
                viewModel.getStories("Bearer $token")
            }
        }
    }

    private fun setupAdapter() {
        adapter = ListStoriesAdapter()
        viewModel.getDataStories().observe(this) { data ->
            if (data != null) {
                adapter.setListStories(data)
                adapter.notifyDataSetChanged()
                showListStories()
            } else {
                Toast.makeText(this, getString(R.string.data_notfound), Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.storiesResponse.observe(this) { response ->
            if (response.isSuccessful) {
                Log.d("getStories", response.message())
            } else {
                Toast.makeText(this, response.message(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showListStories() {
        binding.apply {
            rvStories.layoutManager = LinearLayoutManager(this@HomeActivity)
            rvStories.setHasFixedSize(true)
            rvStories.adapter = adapter
        }
    }

    private fun logout() {
        resetSessionUser()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun resetSessionUser() {
        session.setSessionLogin("", false)
        val logoutPref = getSharedPreferences(NAME_KEY_SESSION, MODE_PRIVATE)
        val session: SharedPreferences.Editor = logoutPref.edit()
        session.putString(KEY_SESSION, "false")
        session.apply()
    }

    private fun showLoading(loading: Boolean){
        if(loading) binding.loading.visibility = View.VISIBLE
        else binding.loading.visibility = View.GONE
    }

    companion object {
        private const val NAME_KEY_SESSION = "user_session"
        private const val KEY_SESSION = "session"
    }

}