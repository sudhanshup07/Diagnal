package com.example.diagnal.ui.listing

import com.example.diagnal.domain.model.ListingResponse


sealed class GetContentListEvent{

    class  Success(val data: ListingResponse): GetContentListEvent()
    object Loading: GetContentListEvent()
    class Failure(val errorText: String): GetContentListEvent()
    object Empty: GetContentListEvent()
}
