package com.example.diagnal.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diagnal.common.Resource
import com.example.diagnal.domain.model.ListingResponse
import com.example.diagnal.domain.repository.MainRepository
import com.example.diagnal.ui.listing.GetContentListEvent
import com.example.diagnal.ui.search.GetSearchListEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {

    private val _listingResponse = MutableStateFlow<GetContentListEvent>(GetContentListEvent.Empty)
    val listingResponse: StateFlow<GetContentListEvent> = _listingResponse

    private val _searchList = MutableStateFlow<GetSearchListEvent>(GetSearchListEvent.Empty)
    val searchList: StateFlow<GetSearchListEvent> = _searchList

    private var listingPage = 1
    private var listingPaginationResponse: ListingResponse? = null

    init {
        Log.e("INIT", "YES")
        getCategories()
    }

    fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {

            when (val response = repository.getListing(listingPage)) {

                is Resource.Success -> {

                    listingPage++
                    if(listingPaginationResponse == null) {
                        listingPaginationResponse = response.data
                    } else {
                        val oldArticles = listingPaginationResponse?.page?.contentItems?.content
                        val newArticles = response.data?.page?.contentItems?.content
                        oldArticles?.addAll(newArticles!!)
                    }

                    _listingResponse.value =
                        GetContentListEvent.Success(listingPaginationResponse!!)
                }

                else -> Unit
            }
        }
    }

    private var searchJob: Job? = null

    fun onSearch(query: String) {

        if(query == "" || query.length<=2){
            return
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)

            val searchList = listingPaginationResponse!!.page.contentItems.content.filter {
                it.name.lowercase() == query.lowercase() ||
                        it.name.lowercase().contains(query.lowercase())
            }

            _searchList.value = GetSearchListEvent.Success(searchList)
        }
    }


}