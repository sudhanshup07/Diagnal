package com.example.diagnal.ui.listing

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diagnal.R
import com.example.diagnal.common.MarginItemDecoration
import com.example.diagnal.databinding.FragmentListingBinding
import com.example.diagnal.domain.model.ListingResponse
import com.example.diagnal.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListingFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentListingBinding? = null
    private val binding get() = _binding!!

    lateinit var listingResponse: ListingResponse
    private lateinit var adapter: ListingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListingBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.addItemDecoration(MarginItemDecoration())
        binding.recyclerView.addOnScrollListener(scrollListener)
        adapter = ListingAdapter(
            ListingAdapter.OnClickListener{ content, _->
                Toast.makeText(requireContext(), "Clicked ${content.name}" , Toast.LENGTH_SHORT).show()
            })
        binding.recyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        setObserver()

        binding.imageBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.imageSearch.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setObserver() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.listingResponse.collect {
                    when (it) {
                        is GetContentListEvent.Success -> {
                            listingResponse = it.data
                            val list = it.data.page.contentItems.content
                            (binding.recyclerView.adapter as ListingAdapter).submitList(list)
                                adapter.notifyDataSetChanged()
                        }
                        else -> Unit
                    }
                }
            }
        }
    }


    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as GridLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= listingResponse.page.pageSize
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                viewModel.getCategories()
                isScrolling = false
            } else {
                binding.recyclerView.setPadding(0, 0, 0, 0)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}