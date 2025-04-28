package com.example.indiagovnews.model

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("totalArticles")
    val totalArticles: Int,
    @SerializedName("articles")
    val articles: List<NewsArticle>
)

data class NewsArticle(
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("url")
    val url: String,
    @SerializedName("image")
    val imageUrl: String?,
    @SerializedName("publishedAt")
    val publishedAt: String,
    @SerializedName("source")
    val source: Source,
    @SerializedName("language")
    val language: String = "en", 
    @SerializedName("category")
    val category: String = "", 
    @SerializedName("sentiment")
    val sentiment: Float? = null, 
    @SerializedName("region")
    val region: String = "National" 
) {
    val id: String
        get() = url 
    
    val articleUrl: String
        get() = url
}

data class Source(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String? = null
)
