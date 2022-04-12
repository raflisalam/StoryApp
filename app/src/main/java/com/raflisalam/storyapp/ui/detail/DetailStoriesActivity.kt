package com.raflisalam.storyapp.ui.detail

import android.os.Bundle
import android.text.Html
import android.text.format.DateUtils
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.raflisalam.storyapp.R
import com.raflisalam.storyapp.databinding.ActivityDetailStoriesBinding
import com.raflisalam.storyapp.model.stories.get.StoriesUsers
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DetailStoriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoriesBinding
    private var timeConvert: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        getParcelableData()
    }

    private fun setupActionBar() {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        actionBar?.title = (Html.fromHtml("<font color=\"black\">" + getString(R.string.detail_activity) + "</font" ))
    }


    private fun getParcelableData() {
        val dataStories: StoriesUsers = intent.getParcelableExtra<StoriesUsers>(DETAIL_STORIES) as StoriesUsers
        setDetailData(dataStories)
    }

    private fun setDetailData(data: StoriesUsers) {
        val dataLikes = intent.getStringExtra(DETAIL_LIKES)
        val dataComment = intent.getStringExtra(DETAIL_COMMENT)

            binding.apply {
            Glide.with(this@DetailStoriesActivity)
                .load(data.photoUrl)
                .apply(RequestOptions())
                .into(photoStories)
            nameUser.text = data.name
            descStories.text = data.description
            timeUpload.text = timeConvert
            likes.text = dataLikes
            comment.text = dataComment

            val time = data.createdAt.toString()
            convertTime(time)
        }
    }

    private fun convertTime(time: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        sdf.timeZone = (TimeZone.getTimeZone("GMT+8"))
        try {
            val time: Long = sdf.parse(time).time
            val now: Long = System.currentTimeMillis()
            val timeAgo: CharSequence = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
            timeConvert = timeAgo.toString()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return timeConvert
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val DETAIL_STORIES = "stories"
        const val DETAIL_LIKES = "likes"
        const val DETAIL_COMMENT = "comment"
    }
}