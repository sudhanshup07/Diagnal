package com.example.diagnal.ui.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.diagnal.common.MarginItemDecoration
import com.example.diagnal.databinding.FragmentSearchBinding
import com.example.diagnal.ui.listing.GetSearchListEvent
import com.example.diagnal.ui.listing.ListingAdapter
import com.example.diagnal.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var adapter:ListingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        binding.editTextTextPersonName.doAfterTextChanged {
            viewModel.onSearch(it.toString())
        }

        lifecycleScope.launch {
            viewModel.searchList.collect {
                when(it){
                    is GetSearchListEvent.Success -> {
                        Log.e("KYA_RESULT", it.data.size.toString())
                        (binding.searchRecyclerView.adapter as ListingAdapter).submitList(it.data)
                        adapter.notifyDataSetChanged()
                    }

                    else -> Unit
                }
            }
        }
    }
    private fun setUpRecyclerView() {
        binding.searchRecyclerView.addItemDecoration(MarginItemDecoration())
        adapter = ListingAdapter(
            ListingAdapter.OnClickListener{ content, _->
                Toast.makeText(requireContext(), "Clicked ${content.name}" , Toast.LENGTH_SHORT).show()
            })
        binding.searchRecyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}