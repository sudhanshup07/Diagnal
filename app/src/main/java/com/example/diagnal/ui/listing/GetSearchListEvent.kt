package com.example.diagnal.ui.listing

import com.example.diagnal.domain.model.Content
import com.example.diagnal.domain.model.ListingResponse


sealed class GetSearchListEvent{

    class  Success(val data: List<Content>): GetSearchListEvent()
    object Loading: GetSearchListEvent()
    class Failure(val errorText: String): GetSearchListEvent()
    object Empty: GetSearchListEvent()
}
