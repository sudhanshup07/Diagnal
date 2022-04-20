package com.example.diagnal.domain.repository

import com.example.diagnal.common.Resource
import com.example.diagnal.domain.model.ListingResponse

interface MainRepository {
    suspend fun getListing(page: Int): Resource<ListingResponse>
}