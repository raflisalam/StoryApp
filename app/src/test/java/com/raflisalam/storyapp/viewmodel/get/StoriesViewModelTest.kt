package com.raflisalam.storyapp.viewmodel.get

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.raflisalam.storyapp.DummyData
import com.raflisalam.storyapp.TestCoroutineRule
import com.raflisalam.storyapp.data.repository.Repository
import com.raflisalam.storyapp.getOrAwaitValue
import com.raflisalam.storyapp.model.stories.StoriesError
import com.raflisalam.storyapp.viewmodel.get.maps.MapsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doThrow

@OptIn(ExperimentalCoroutinesApi::class)
class MapsViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val repository: Repository = Mockito.mock(Repository::class.java)
    private lateinit var mapsViewModel: MapsViewModel

    @Before
    fun setup() {
        mapsViewModel = MapsViewModel(repository)
    }

    @Test
    fun `when getStoriesMaps success didn't throw error message`() = testCoroutineRule.runBlockingTest {
        val token = "token dummy"
        val fakeData = DummyData.getListStoriesModel()

        `when`(repository.storiesMaps(token)).thenReturn(fakeData)
        mapsViewModel.getStoriesMap(token)

        Mockito.verify(repository).storiesMaps(token)

        val storiesResult = mapsViewModel.listMaps.getOrAwaitValue()
        assertNotNull(storiesResult)
        assertTrue(storiesResult[0].name == "fake name-0")
    }

    @Test
    fun `when getStoriesMaps failed to get data and throw error`() = testCoroutineRule.runBlockingTest {
        val token = "token dummy"
        doThrow(StoriesError("Error")).`when`(repository).storiesMaps(token)
        mapsViewModel.getStoriesMap(token)

        Mockito.verify(repository).storiesMaps(token)
        assertTrue(mapsViewModel.message.getOrAwaitValue() == "Error")
    }
}