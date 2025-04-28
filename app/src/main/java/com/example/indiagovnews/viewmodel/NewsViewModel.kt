package com.example.indiagovnews.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.indiagovnews.model.NewsArticle
import com.example.indiagovnews.model.NewsCategory
import com.example.indiagovnews.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class NewsViewModel(
    private val repository: NewsRepository
) : ViewModel() {

    private val _newsArticles = MutableLiveData<List<NewsArticle>>()
    val newsArticles: LiveData<List<NewsArticle>> = _newsArticles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private var currentPage = 1
    private var currentCategory: NewsCategory = NewsCategory.GENERAL
    private var isLoadingMore = false
    private var hasMorePages = true
    private val currentArticles = mutableListOf<NewsArticle>()

    fun loadNewsByCategory(category: NewsCategory) {
        currentCategory = category
        currentPage = 1
        currentArticles.clear()
        hasMorePages = true
        loadNews(false)
    }

    fun loadMoreNews() {
        if (!isLoadingMore && hasMorePages) {
            loadNews(true)
        }
    }

    private fun loadNews(loadMore: Boolean) {
        viewModelScope.launch {
            try {
                if (loadMore) {
                    isLoadingMore = true
                } else {
                    _isLoading.value = true
                }
                _error.value = null
                
                Log.d("NewsViewModel", "Loading news for category: ${currentCategory.name}, page: $currentPage")
                val articles = withContext(Dispatchers.IO) {
                    repository.getNewsByCategory(currentCategory.name, currentPage)
                }

                if (articles.isEmpty()) {
                    hasMorePages = false
                } else {
                    if (loadMore) {
                        currentArticles.addAll(articles)
                        _newsArticles.value = currentArticles.toList()
                        currentPage++
                    } else {
                        currentArticles.clear()
                        currentArticles.addAll(articles)
                        _newsArticles.value = articles
                        currentPage = 2
                    }
                }
                Log.d("NewsViewModel", "Loaded ${articles.size} articles")
            } catch (e: IOException) {
                Log.e("NewsViewModel", "Network error", e)
                _error.value = "Network error. Please check your internet connection."
            } catch (e: HttpException) {
                Log.e("NewsViewModel", "HTTP error ${e.code()}", e)
                _error.value = when (e.code()) {
                    429 -> "API rate limit exceeded. Please try again later."
                    403 -> "Invalid API key. Please check your configuration."
                    else -> "Error loading news: ${e.message()}"
                }
            } catch (e: Exception) {
                Log.e("NewsViewModel", "Unknown error", e)
                _error.value = "An unexpected error occurred: ${e.message}"
            } finally {
                _isLoading.value = false
                isLoadingMore = false
            }
        }
    }
}
