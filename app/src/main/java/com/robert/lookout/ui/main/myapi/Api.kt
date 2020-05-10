package com.robert.lookout.ui.main.myapi

import com.robert.lookout.ui.main.model.Article
import com.robert.lookout.ui.main.model.NewsResponse
import com.robert.lookout.ui.main.util.Constant.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("v2/top-headlines")
    suspend fun getNews(
        @Query("country")
        country:String,
        @Query("currentPage")
        currentPage:Int=1,
        @Query("apiKey")
        apiKey:String=API_KEY
    ):Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q")
        search:String,
        @Query("currentPage")
        searchPage:Int=1,
        @Query("apiKey")
        apiKey: String= API_KEY

    ):Response<NewsResponse>
}