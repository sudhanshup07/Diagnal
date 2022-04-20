package com.example.diagnal.data.repository

import android.content.Context
import com.example.diagnal.common.JsonUtil
import com.example.diagnal.common.Resource
import com.example.diagnal.data.dto.ListingResponseDto
import com.example.diagnal.domain.model.ListingResponse
import com.example.diagnal.domain.repository.MainRepository
import com.google.gson.Gson
import java.lang.Exception

class MainRepositoryImpl(
    private val context: Context
): MainRepository {

    override suspend fun getListing(page: Int): Resource<ListingResponse> {
        return try {
            val jsonFileString = JsonUtil.getJsonDataFromAsset(context, "content_listing_page$page.json")
            val gson = Gson()
            val testModel = gson.fromJson(jsonFileString, ListingResponseDto::class.java)

            Resource.Success(testModel.toListingResponse())
        }catch (exception: Exception){
            Resource.Error("")
        }
    }
}