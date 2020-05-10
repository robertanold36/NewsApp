package com.robert.lookout.ui.main.model

import com.robert.lookout.ui.main.model.Article


data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)