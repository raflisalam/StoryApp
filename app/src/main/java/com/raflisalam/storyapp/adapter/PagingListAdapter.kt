package com.raflisalam.storyapp.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.raflisalam.storyapp.databinding.ListStoriesBinding
import com.raflisalam.storyapp.model.stories.get.StoriesResponse
import com.raflisalam.storyapp.model.stories.get.StoriesUsers
import com.raflisalam.storyapp.ui.detail.DetailStoriesActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class PagingListAdapter: PagingDataAdapter<StoriesUsers, PagingListAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(val binding: ListStoriesBinding) : RecyclerView.ViewHolder(binding.root) {
        private var timeConvert: String = ""
        private var randomLikes: String = ""
        private var randomComment: String = ""

        fun bind(data: StoriesUsers) {
            binding.apply {
                with(data) {
                    Glide.with(itemView.context)
                        .load(photoUrl)
                        .apply(RequestOptions())
                        .into(photoStories)
                    nameUser.text = name
                    timeUpload.text = timeConvert
                    descStories.text = description
                    likes.text = "$randomLikes Likes"
                    comment.text = "$randomComment Comment"
                    val likess = likes.text
                    val commentt = comment.text
                    val time = createdAt.toString()
                    convertTime(time)
                    itemView.setOnClickListener {
                        val intent = Intent(itemView.context, DetailStoriesActivity::class.java)
                        intent.putExtra(DetailStoriesActivity.DETAIL_STORIES, data)
                        intent.putExtra(DetailStoriesActivity.DETAIL_COMMENT, likess)
                        intent.putExtra(DetailStoriesActivity.DETAIL_LIKES, commentt)

                        val optionsCompat: ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                itemView.context as Activity,
                                androidx.core.util.Pair(icUser, "profile"),
                                androidx.core.util.Pair(nameUser, "name"),
                                androidx.core.util.Pair(timeUpload, "time"),
                                androidx.core.util.Pair(descStories, "desc"),
                                androidx.core.util.Pair(photoStories, "photo"),
                                androidx.core.util.Pair(icLike, "iconLike"),
                                androidx.core.util.Pair(likes, "likes"),
                                androidx.core.util.Pair(icComment, "iconComment"),
                                androidx.core.util.Pair(comment, "comment")
                            )
                        itemView.context.startActivity(intent, optionsCompat.toBundle())
                    }
                }
            }
        }

        private fun randomLikes(): String {
            val minLikes = 100
            val maxLikes = 1500
            val random1 = Random().nextInt((maxLikes - minLikes) + 1) + minLikes
            randomLikes = random1.toString()

            return randomLikes
        }

        private fun randomComment(): String {
            val minComment = 10
            val maxComment = 30
            val random2 = Random().nextInt((maxComment - minComment) + 1) + minComment
            randomComment = random2.toString()

            return randomComment
        }

        @SuppressLint("SimpleDateFormat")
        private fun convertTime(time: String): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            sdf.timeZone = (TimeZone.getTimeZone("UTC +7"))
            try {
                val time: Long = sdf.parse(time).time
                val now: Long = System.currentTimeMillis()
                val timeAgo: CharSequence = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
                timeConvert = timeAgo.toString()
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            randomLikes()
            randomComment()
            return timeConvert
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    companion object {
         val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoriesUsers>() {
            override fun areItemsTheSame(oldItem: StoriesUsers, newItem: StoriesUsers): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: StoriesUsers, newItem: StoriesUsers): Boolean {
                return oldItem.name == newItem.name
            }

        }
    }

}