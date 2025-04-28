package com.example.indiagovnews.api

import com.example.indiagovnews.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    companion object {
        const val BASE_URL = "https://gnews.io/api/v4/"
        const val API_KEY = "c7591366e49342593a544e3e755cdbb0"
        const val PAGE_SIZE = 10
    }

    @GET("search")
    suspend fun getNewsByCategory(
        @Query("q") query: String,
        @Query("country") country: String = "in",
        @Query("lang") language: String = "en",
        @Query("apikey") apiKey: String = API_KEY,
        @Query("max") max: Int = PAGE_SIZE,
        @Query("page") page: Int = 1
    ): NewsResponse

    @GET("top-headlines")
    suspend fun getLatestNews(
        @Query("country") country: String = "in",
        @Query("lang") language: String = "en",
        @Query("apikey") apiKey: String = API_KEY,
        @Query("max") max: Int = PAGE_SIZE,
        @Query("page") page: Int = 1
    ): NewsResponse
}
