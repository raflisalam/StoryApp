package com.raflisalam.storyapp.viewmodel.get

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.raflisalam.storyapp.DummyData
import com.raflisalam.storyapp.TestCoroutineRule
import com.raflisalam.storyapp.adapter.PagingListAdapter
import com.raflisalam.storyapp.getOrAwaitValue
import com.raflisalam.storyapp.model.stories.get.StoriesUsers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class PagingViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var pagingViewModel: PagingViewModel

    @Test
    fun `when stories should not null or empty`() = testCoroutineRule.runBlockingTest {
        val stories = DummyData.getListStoriesModel()
        val dataSource = DataSourcePageTest.snapshot(stories)
        val liveDataDummy = MutableLiveData<PagingData<StoriesUsers>>().apply {
            value = dataSource
        }

        `when`(pagingViewModel.getStories).thenReturn(liveDataDummy)
        val actualDataStories = pagingViewModel.getStories.getOrAwaitValue()

        val differCallback = AsyncPagingDataDiffer(
            diffCallback = PagingListAdapter.DIFF_CALLBACK,
            updateCallback = loopListUpdateCallback,
            mainDispatcher = testCoroutineRule.dispatcher,
            workerDispatcher = testCoroutineRule.dispatcher
        )
        differCallback.submitData(actualDataStories)
        advanceUntilIdle()
        Mockito.verify(pagingViewModel).getStories
        assertNotNull(differCallback.snapshot())
        assertEquals(stories.size, differCallback.snapshot().size)
    }
}

class DataSourcePageTest private constructor() : PagingSource<Int, LiveData<List<StoriesUsers>>>() {
    companion object {
        fun snapshot(stories: List<StoriesUsers>): PagingData<StoriesUsers> {
            return PagingData.from(stories)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoriesUsers>>>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoriesUsers>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val loopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {
    }

    override fun onRemoved(position: Int, count: Int) {
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
    }

}