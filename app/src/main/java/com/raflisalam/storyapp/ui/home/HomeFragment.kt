package com.raflisalam.storyapp.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.raflisalam.storyapp.R
import com.raflisalam.storyapp.adapter.ListStoriesAdapter
import com.raflisalam.storyapp.data.repository.StoriesRepo
import com.raflisalam.storyapp.databinding.FragmentHomeBinding
import com.raflisalam.storyapp.pref.UserSession
import com.raflisalam.storyapp.viewmodel.get.StoriesFactoryViewModel
import com.raflisalam.storyapp.viewmodel.get.StoriesViewModel
import com.raflisalam.storyapp.viewmodel.session.SessionFactoryViewModel
import com.raflisalam.storyapp.viewmodel.session.SessionViewModel

class HomeFragment : Fragment() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var userSession: UserSession
    private lateinit var adapter: ListStoriesAdapter
    private lateinit var viewModel: StoriesViewModel
    private lateinit var session: SessionViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupAdapter()
    }

    private fun setupViewModel() {
        val repository = StoriesRepo()
        val viewModelFactory = StoriesFactoryViewModel(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[StoriesViewModel::class.java]
        userSession = UserSession.newInstance(requireContext().dataStore)
        session = ViewModelProvider(this, SessionFactoryViewModel(userSession))[SessionViewModel::class.java]

        viewModel.loading.observe(viewLifecycleOwner){
            showLoading(it)
        }

        viewModel.storiesResponse.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                Log.d("getStories", it.message())
            } else {
                Toast.makeText(context, it.message(), Toast.LENGTH_SHORT).show()
            }
        }

        userSession.userToken.asLiveData().observe(viewLifecycleOwner) { token ->
            token?.let {
                viewModel.getStories("Bearer $token")
            }
        }
    }

    private fun setupAdapter() {
        adapter = ListStoriesAdapter()
        viewModel.listStories.observe(viewLifecycleOwner) { data->
            if (data != null) {
                adapter.setListStories(data)
                adapter.notifyDataSetChanged()
                showListStories()
            } else {
                Toast.makeText(context, getString(R.string.data_notfound), Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun showListStories() {
        binding.apply {
            rvStories.layoutManager = LinearLayoutManager(context)
            rvStories.setHasFixedSize(true)
            rvStories.adapter = adapter
        }
    }

    private fun showLoading(loading: Boolean){
        if(loading) binding.loading.visibility = View.VISIBLE
        else binding.loading.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}