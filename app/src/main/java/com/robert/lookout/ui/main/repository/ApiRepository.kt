package com.robert.lookout.ui.main.repository


import com.robert.lookout.ui.main.myapi.RetrofitInstance
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRepository @Inject constructor() {

    suspend fun getNews(country:String,currentPage:Int)=RetrofitInstance.
        api.getNews(country,currentPage)

    suspend fun searchNews(searchString: String,currentPage: Int)=RetrofitInstance.
        api.searchNews(searchString,currentPage)

}