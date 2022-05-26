package com.raflisalam.storyapp.viewmodel.post.stories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.raflisalam.storyapp.TestCoroutineRule
import com.raflisalam.storyapp.data.repository.Repository
import com.raflisalam.storyapp.getOrAwaitValue
import com.raflisalam.storyapp.model.stories.StoriesError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class PostStoriesViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val repository: Repository = mock(Repository::class.java)
    private lateinit var postStoriesViewModel: PostStoriesViewModel

    @Before
    fun setup() {
        postStoriesViewModel  = PostStoriesViewModel(repository)
    }

    @Test
    fun `when posting stories success should throw IsSuccess`() = testCoroutineRule.runBlockingTest {
        val token = "token dummy"
        val fileDummy = MultipartBody.Part.createFormData("file-photo", "fileDummy")
        val desc = "description dummy".toRequestBody("text/plain".toMediaType())

        postStoriesViewModel.postStories(token, fileDummy, desc)
        Mockito.verify(repository).postStories(token, fileDummy, desc)
        assertTrue(postStoriesViewModel.isSuccess.getOrAwaitValue())
    }

    @Test
    fun `when posting stories failed and throw error`() = testCoroutineRule.runBlockingTest {
        val token = "token dummy"
        val fileDummy = MultipartBody.Part.createFormData("file-photo", "fileDummy")
        val desc = "description dummy".toRequestBody("text/plain".toMediaType())

        Mockito.doThrow(StoriesError("Error")).`when`(repository).postStories(token, fileDummy, desc)
        postStoriesViewModel.postStories(token, fileDummy, desc)
        Mockito.verify(repository).postStories(token, fileDummy, desc)
        assertTrue(postStoriesViewModel.message.getOrAwaitValue() == "Error")
    }
}