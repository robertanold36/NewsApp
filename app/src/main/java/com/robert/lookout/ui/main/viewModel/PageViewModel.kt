package com.robert.lookout.ui.main.viewModel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robert.lookout.ui.main.di.ActivityScope
import com.robert.lookout.ui.main.model.NewsResponse
import com.robert.lookout.ui.main.repository.ApiRepository
import kotlinx.coroutines.launch
import com.robert.lookout.ui.main.util.Resources
import retrofit2.Response
import javax.inject.Inject

@ActivityScope
class PageViewModel @Inject constructor(private val repository: ApiRepository) : ViewModel() {

    val topHeadLineNews: MutableLiveData<Resources<NewsResponse>> = MutableLiveData()
    var breakingPage = 1
    var breakingNewsResponse: NewsResponse? = null

    val searchNewsData: MutableLiveData<Resources<NewsResponse>> = MutableLiveData()
    var searchBreakingPage = 1
    var searchNewsResponse: NewsResponse? = null


    init {
        getNews("us")
    }

    fun getNews(country: String) = viewModelScope.launch {
        topHeadLineNews.postValue(Resources.Loading())
        val response = repository.getNews(country, 1)
        topHeadLineNews.postValue(handleTopHeadLinesNews(response))

    }

    fun searchNews(searchString: String)=viewModelScope.launch {
        searchNewsData.postValue(Resources.Loading())
        val response=repository.searchNews(searchString,1)
        searchNewsData.postValue(handleNewsResponse(response))

    }


    private fun handleTopHeadLinesNews(response: Response<NewsResponse>): Resources<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                breakingPage++

                if (breakingNewsResponse == null) {
                    breakingNewsResponse = it
                } else {
                    val oldNews = breakingNewsResponse?.articles
                    val newInfo = it.articles
                    oldNews?.addAll(newInfo)
                }

                return Resources.Success(breakingNewsResponse ?: it)
            }
        }
        return Resources.Error(response.message())
    }


    private fun handleNewsResponse(response:Response<NewsResponse>):Resources<NewsResponse>{
        if(response.isSuccessful)
        {
            response.body()?.let {
                searchBreakingPage++

                if(searchNewsResponse==null){
                    searchNewsResponse=it
                }
                else{
                    val oldItem=searchNewsResponse?.articles
                    val newItem=it.articles
                    oldItem?.addAll(newItem)
                }

                return Resources.Success(searchNewsResponse?:it)
            }
        }

        return Resources.Error(response.message())
    }
}