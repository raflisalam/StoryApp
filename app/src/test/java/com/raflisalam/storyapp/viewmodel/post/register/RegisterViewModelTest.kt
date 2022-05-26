package com.raflisalam.storyapp.viewmodel.post.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.raflisalam.storyapp.TestCoroutineRule
import com.raflisalam.storyapp.getOrAwaitValue
import com.raflisalam.storyapp.model.auth.register.RegisterUser
import com.raflisalam.storyapp.data.repository.Repository
import com.raflisalam.storyapp.model.auth.AuthError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.doThrow
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var repository: Repository
    private lateinit var registerViewModel: RegisterViewModel

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(repository)
    }

    @Test
    fun `when register response is success`() = testCoroutineRule. runBlockingTest {
        val dataDummy = RegisterUser("fake name", "fakeemail@gmail.com", "fakepassword")
        registerViewModel.registerUser(dataDummy)
        Mockito.verify(repository).registerUser(dataDummy)
        assertTrue(registerViewModel.error.getOrAwaitValue())
    }

    @Test
    fun `when register response throw error`() = testCoroutineRule.runBlockingTest {
        val dataDummy = RegisterUser("fake name", "fakeemail@gmail.com", "fakepassword")
        doThrow(AuthError("Error")).`when`(repository).registerUser(dataDummy)
        registerViewModel.registerUser(dataDummy)
        Mockito.verify(repository).registerUser(dataDummy)
        assertTrue(registerViewModel.message.getOrAwaitValue() == "Error")
    }
}