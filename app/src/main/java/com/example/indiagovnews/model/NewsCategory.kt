package com.example.indiagovnews.model

enum class NewsCategory {
    GENERAL,
    POLITICS,
    SPORTS,
    TECHNOLOGY,
    ARMY,
    ECONOMY,
    HEALTH,
    EDUCATION;

    companion object {
        fun fromString(value: String): NewsCategory {
            return try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                GENERAL
            }
        }
    }
}
