package ru.bulat.mukhutdinov.kotlintestapp.presentation.datasource.contentprovider

import android.content.SearchRecentSuggestionsProvider

class SuggestionProvider : SearchRecentSuggestionsProvider() {

    companion object {
        const val AUTHORITY = "ru.bulat.mukhutdinov.kotlintestapp.SuggestionProvider"
        const val MODE = DATABASE_MODE_QUERIES
    }

    init {
        setupSuggestions(AUTHORITY, MODE);
    }
}