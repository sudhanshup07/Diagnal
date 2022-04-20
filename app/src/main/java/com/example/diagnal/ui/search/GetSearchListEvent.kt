package com.example.diagnal.ui.search

import com.example.diagnal.domain.model.Content


sealed class GetSearchListEvent{

    class  Success(val data: List<Content>): GetSearchListEvent()
    object Loading: GetSearchListEvent()
    class Failure(val errorText: String): GetSearchListEvent()
    object Empty: GetSearchListEvent()
}
