package com.example.diagnal.data.dto

import com.example.diagnal.domain.model.Content
import com.example.diagnal.domain.model.ContentItems
import com.example.diagnal.domain.model.ListingResponse
import com.example.diagnal.domain.model.Page
import com.google.gson.annotations.SerializedName

data class ListingResponseDto(
    val page: PageDto
){
    fun toListingResponse():ListingResponse{
        return ListingResponse(
            page = page.toPage()
        )
    }
}

data class PageDto(
    @SerializedName("content-items")
    val contentItems: ContentItemsDto,

    @SerializedName("page-num")
    val pageNum: Int,

    @SerializedName("page-size")
    val pageSize: Int,

    val title: String,

    @SerializedName("total-content-items")
    val totalContentItems: Int
){
    fun toPage():Page{
        return Page(
            contentItems = contentItems.toContentItems(),
            pageNum = pageNum,
            pageSize = pageSize,
            title = title,
            totalContentItems = totalContentItems
        )
    }
}

data class ContentItemsDto(
    val content: List<ContentDto>
){
    fun toContentItems():ContentItems{
        return ContentItems(
            content = content.map { it.toContent() } as MutableList<Content>
        )
    }
}

data class ContentDto(
    val name: String,

    @SerializedName("poster-image")
    val posterImage: String
){
    fun toContent():Content{
        return Content(
            name = name,
            posterImage = posterImage
        )
    }
}