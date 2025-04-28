package com.example.indiagovnews.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.indiagovnews.model.NewsCategory

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var isLoggedIn: Boolean
        get() = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_LOGGED_IN, value).apply()

    var userName: String
        get() = prefs.getString(KEY_USER_NAME, "") ?: ""
        set(value) = prefs.edit().putString(KEY_USER_NAME, value).apply()

    var userEmail: String
        get() = prefs.getString(KEY_USER_EMAIL, "") ?: ""
        set(value) = prefs.edit().putString(KEY_USER_EMAIL, value).apply()

    var userState: String
        get() = prefs.getString(KEY_USER_STATE, "") ?: ""
        set(value) = prefs.edit().putString(KEY_USER_STATE, value).apply()

    var defaultLanguage: String
        get() = prefs.getString(KEY_DEFAULT_LANGUAGE, "en") ?: "en"
        set(value) = prefs.edit().putString(KEY_DEFAULT_LANGUAGE, value).apply()

    var defaultCategory: NewsCategory
        get() = NewsCategory.fromString(prefs.getString(KEY_DEFAULT_CATEGORY, NewsCategory.GENERAL.name) ?: NewsCategory.GENERAL.name)
        set(value) = prefs.edit().putString(KEY_DEFAULT_CATEGORY, value.name).apply()

    var defaultRegion: String
        get() = prefs.getString(KEY_DEFAULT_REGION, "in") ?: "in"
        set(value) = prefs.edit().putString(KEY_DEFAULT_REGION, value).apply()

    var lastTabPosition: Int
        get() = prefs.getInt(KEY_LAST_TAB, 0)
        set(value) = prefs.edit().putInt(KEY_LAST_TAB, value).apply()

    var isDarkMode: Boolean
        get() = prefs.getBoolean(KEY_DARK_MODE, false)
        set(value) = prefs.edit().putBoolean(KEY_DARK_MODE, value).apply()

    var textSize: Float
        get() = prefs.getFloat(KEY_TEXT_SIZE, 1.0f)
        set(value) = prefs.edit().putFloat(KEY_TEXT_SIZE, value).apply()

    var lastNewsFetch: Long
        get() = prefs.getLong(KEY_LAST_FETCH, 0)
        set(value) = prefs.edit().putLong(KEY_LAST_FETCH, value).apply()

    var appRating: Float
        get() = prefs.getFloat(KEY_APP_RATING, 0.0f)
        set(value) = prefs.edit().putFloat(KEY_APP_RATING, value).apply()

    var appFeedback: String
        get() = prefs.getString(KEY_APP_FEEDBACK, "") ?: ""
        set(value) = prefs.edit().putString(KEY_APP_FEEDBACK, value).apply()

    fun addReadArticle(articleId: String) {
        val readArticles = getReadArticles().toMutableSet()
        readArticles.add(articleId)
        prefs.edit().putStringSet(KEY_READ_ARTICLES, readArticles).apply()
    }

    fun isArticleRead(articleId: String): Boolean {
        return getReadArticles().contains(articleId)
    }

    private fun getReadArticles(): Set<String> {
        return prefs.getStringSet(KEY_READ_ARTICLES, emptySet()) ?: emptySet()
    }

    fun clearLoginData() {
        prefs.edit().apply {
            remove(KEY_IS_LOGGED_IN)
            remove(KEY_USER_NAME)
            remove(KEY_USER_EMAIL)
            remove(KEY_USER_STATE)
        }.apply()
    }

    companion object {
        private const val PREFS_NAME = "india_gov_news_prefs"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_STATE = "user_state"
        private const val KEY_DEFAULT_LANGUAGE = "default_language"
        private const val KEY_DEFAULT_CATEGORY = "default_category"
        private const val KEY_DEFAULT_REGION = "default_region"
        private const val KEY_LAST_TAB = "last_tab"
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_TEXT_SIZE = "text_size"
        private const val KEY_LAST_FETCH = "last_fetch"
        private const val KEY_READ_ARTICLES = "read_articles"
        private const val KEY_APP_RATING = "app_rating"
        private const val KEY_APP_FEEDBACK = "app_feedback"
    }
}
