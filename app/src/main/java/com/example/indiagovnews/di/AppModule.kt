package com.example.indiagovnews.di

import com.example.indiagovnews.api.NewsApiService
import com.example.indiagovnews.repository.NewsRepository
import com.example.indiagovnews.viewmodel.NewsViewModelFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object AppModule {
    private const val BASE_URL = "https://gnews.io/api/v4/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val url = original.url.newBuilder()
                .addQueryParameter("lang", "en,hi")
                .addQueryParameter("country", "in")
                .build()
            val request = original.newBuilder()
                .url(url)
                .build()
            chain.proceed(request)
        }
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val newsApiService: NewsApiService = retrofit.create(NewsApiService::class.java)
    private val newsRepository = NewsRepository(newsApiService)
    
    val newsViewModelFactory = NewsViewModelFactory(newsRepository)
}
