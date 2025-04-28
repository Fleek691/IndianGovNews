package com.example.indiagovnews.repository

import android.util.Log
import com.example.indiagovnews.api.NewsApiService
import com.example.indiagovnews.model.NewsArticle

class NewsRepository(private val newsApiService: NewsApiService) {
    
    suspend fun getNewsByCategory(category: String, page: Int = 1): List<NewsArticle> {
        return try {
            Log.d("NewsRepository", "Fetching news for category: $category, page: $page")
            val searchQuery = when (category.lowercase()) {
                "general" -> "india government"
                "politics" -> "india politics"
                "sports" -> "india sports"
                "technology" -> "india technology"
                "army" -> "india military defense"
                "economy" -> "india economy business"
                "health" -> "india healthcare medical"
                "education" -> "india education"
                else -> "india government"
            }
            
            val response = newsApiService.getNewsByCategory(searchQuery, page = page)
            Log.d("NewsRepository", "Received ${response.articles.size} articles")
            response.articles
        } catch (e: Exception) {
            Log.e("NewsRepository", "Error fetching news", e)
            throw e
        }
    }

    suspend fun getLatestNews(page: Int = 1): List<NewsArticle> {
        return try {
            Log.d("NewsRepository", "Fetching latest news, page: $page")
            val response = newsApiService.getLatestNews(page = page)
            Log.d("NewsRepository", "Received ${response.articles.size} articles")
            response.articles
        } catch (e: Exception) {
            Log.e("NewsRepository", "Error fetching latest news", e)
            throw e
        }
    }
}
