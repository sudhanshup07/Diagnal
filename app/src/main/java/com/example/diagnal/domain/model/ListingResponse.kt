package com.example.diagnal.domain.model

data class ListingResponse(
    val page: Page
)

data class Page(
    val contentItems: ContentItems,
    val pageNum: Int,
    val pageSize: Int,

    val title: String,
    val totalContentItems: Int
)

data class ContentItems(
    val content: MutableList<Content>
)

data class Content(
    val name: String,
    val posterImage: String
)