package com.raflisalam.storyapp.ui.stories

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.raflisalam.storyapp.R
import com.raflisalam.storyapp.databinding.ActivityPostStoriesBinding
import com.raflisalam.storyapp.pref.UserSession
import com.raflisalam.storyapp.ui.home.HomeActivity
import com.raflisalam.storyapp.ui.utils.convertUriToFile
import com.raflisalam.storyapp.viewmodel.post.stories.PostStoriesViewModel
import com.raflisalam.storyapp.viewmodel.post.stories.PostStoriesViewModelFactory
import com.raflisalam.storyapp.viewmodel.session.SessionFactoryViewModel
import com.raflisalam.storyapp.viewmodel.session.SessionViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class PostStoriesActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

    private lateinit var binding: ActivityPostStoriesBinding
    private val viewModel: PostStoriesViewModel by viewModels {
        PostStoriesViewModelFactory.getInstance(this)
    }
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

            viewModel.postStories(token!!, imageMultipart, description)

            viewModel.isSuccess.observe(this) { isSuccess ->
                if (isSuccess) {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
            }
            viewModel.message.observe(this) { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
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