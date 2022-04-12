package com.raflisalam.storyapp.ui.stories

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.raflisalam.storyapp.R
import com.raflisalam.storyapp.api.ApiClient
import com.raflisalam.storyapp.databinding.ActivityPostStoriesBinding
import com.raflisalam.storyapp.model.stories.post.PostStoriesResponse
import com.raflisalam.storyapp.pref.UserSession
import com.raflisalam.storyapp.repository.Repository
import com.raflisalam.storyapp.ui.home.HomeActivity
import com.raflisalam.storyapp.ui.utils.convertUriToFile
import com.raflisalam.storyapp.viewmodel.post.stories.PostStoriesViewModel
import com.raflisalam.storyapp.viewmodel.post.stories.PostStoriesViewModelFactory
import com.raflisalam.storyapp.viewmodel.session.SessionFactoryViewModel
import com.raflisalam.storyapp.viewmodel.session.SessionViewModel
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PostStoriesActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

    private lateinit var binding: ActivityPostStoriesBinding
    private lateinit var viewModel: PostStoriesViewModel
    private lateinit var session: SessionViewModel
    private lateinit var userSession: UserSession

    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostStoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupActionBar()
        getUriData()

    }

    private fun setupViewModel() {
        val repository = Repository()
        val viewModelFactory = PostStoriesViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[PostStoriesViewModel::class.java]

        userSession = UserSession.newInstance(dataStore)
        session = ViewModelProvider(this, SessionFactoryViewModel(userSession))[SessionViewModel::class.java]

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.button_upload -> {
                uploadStories()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_upload_stories, menu)
        return true
    }

    private fun setupActionBar() {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        actionBar?.title = (Html.fromHtml("<font color=\"black\">" + getString(R.string.post_activity) + "</font" ))
    }


    private fun setPhotoStories(uri: Uri) {
        binding.apply {
            Glide.with(this@PostStoriesActivity)
                .load(uri)
                .apply(RequestOptions())
                .into(photoStories)
        }
    }

    private fun getUriData() {
        val uriData: Bundle? = intent.extras
        if (uriData != null && uriData.containsKey(URI_IMAGE)) {
            val convertUri: String? = intent.extras?.getString(URI_IMAGE)
            val uri: Uri = Uri.parse(convertUri)
            setPhotoStories(uri)
            uriToFile(uri)
        }
    }

    private fun uriToFile(uri: Uri): File {
        val uriFile = convertUriToFile(uri, this)
        getFile = uriFile

        return getFile as File
    }

    private fun uploadStories() {
        val descStroies = binding.descForm.text.toString()
        if (getFile != null) {
            val file = getFile as File
            val requestImageFile: RequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData("photo", file.name, requestImageFile)
            val description = descStroies.toRequestBody("text/plain".toMediaType())

            val getTokenPref = getSharedPreferences(NAME_KEY_TOKEN, MODE_PRIVATE)
            val token = getTokenPref.getString(KEY_TOKEN, "")

            viewModel.postStories("Bearer $token", imageMultipart, description)
            viewModel.postResponse.observe(this) { response ->
                if (response.isSuccessful) {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                    Log.d("postSuccess", response.message())
                    Log.d("postSuccess", response.body()?.message.toString())
                    Toast.makeText(this, response.body()?.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, response.message(), Toast.LENGTH_SHORT).show()
                    Log.d("postFail", response.message())
                    Log.d("postFail", response.body()?.message.toString())
                }
            }
        } else {
            Toast.makeText(this, getString(R.string.input_file), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        private const val NAME_KEY_TOKEN = "user_token"
        private const val KEY_TOKEN = "token"

        const val URI_IMAGE = "uri"
    }
}