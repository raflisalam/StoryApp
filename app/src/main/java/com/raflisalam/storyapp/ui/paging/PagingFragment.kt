package com.raflisalam.storyapp.ui.paging

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.raflisalam.storyapp.adapter.LoadingStateAdapter
import com.raflisalam.storyapp.adapter.PagingListAdapter
import com.raflisalam.storyapp.databinding.FragmentHomeBinding
import com.raflisalam.storyapp.databinding.FragmentPagingBinding
import com.raflisalam.storyapp.viewmodel.get.PagingViewModel
import com.raflisalam.storyapp.viewmodel.get.PagingViewModelFactory
import kotlinx.coroutines.launch

class PagingFragment : Fragment() {

    private var _binding: FragmentPagingBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PagingListAdapter
    private val viewModel: PagingViewModel by viewModels {
        PagingViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPagingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
    }

    private fun getData() {
        adapter = PagingListAdapter()
        binding.apply {
            rvStories.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
            lifecycleScope.launch {
                viewModel.getStories.observe(viewLifecycleOwner) {
                    adapter.submitData(lifecycle, it)
                    showListStories()
                }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}