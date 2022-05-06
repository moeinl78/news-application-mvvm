package ir.ariyana.news_application_mvvm.ui.main

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.ariyana.news_application_mvvm.repository.RepositoryMain
import ir.ariyana.news_application_mvvm.repository.model.Article
import ir.ariyana.news_application_mvvm.repository.model.News
import ir.ariyana.news_application_mvvm.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class ViewModelMain(private val app : Application, private val repositoryMain: RepositoryMain) : ViewModel() {

    private val _breakingNewsData = MutableLiveData<Resource<News>>()
    private val _searchNewsData = MutableLiveData<Resource<News>>()

    var breakingNewsPage = 1
    var searchNewsPage = 1

    // use this technique to avoid changing data from ui controller
    val breakingNewsData : LiveData<Resource<News>>
        get() = _breakingNewsData
    val searchNewsData : LiveData<Resource<News>>
        get() = _searchNewsData

    var breakingNewsResponse : News ? = null
    var searchNewsResponse : News ? = null

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode : String) = viewModelScope.launch {
        safeBreakingNewsRequest(countryCode)
    }

    fun getSearchedNews(query : String) = viewModelScope.launch {
        safeSearchNewsRequest(query)
    }

    fun insertArticle(article: Article) = viewModelScope.launch {
        repositoryMain.insertArticle(article)
    }

    fun removeArticle(article: Article) = viewModelScope.launch {
        repositoryMain.removeArticle(article)
    }

    fun receiveArticles() = repositoryMain.receiveArticles()

    private fun handleNewsResponse(response : Response<News>) : Resource<News> {

        if(response.isSuccessful) {

            response.body()?.let { res ->

                breakingNewsPage += 1
                if(breakingNewsResponse == null) {
                    breakingNewsResponse = res
                }
                else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = res.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: res)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchedResponse(response : Response<News>) : Resource<News> {

        if(response.isSuccessful) {

            response.body()?.let { res ->

                searchNewsPage += 1
                if(searchNewsResponse == null) {
                    searchNewsResponse = res
                }
                else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = res.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: res)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeBreakingNewsRequest(countryCode: String) {

        _breakingNewsData.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = repositoryMain.getBreakingNews(countryCode, breakingNewsPage)
                _breakingNewsData.postValue(handleNewsResponse(response))
            }
            else {
                _breakingNewsData.postValue(Resource.Error("No internet Connection"))
            }
        }
        catch (t : Throwable) {
            when(t) {
                is IOException -> _breakingNewsData.postValue(Resource.Error("Network Failure!"))
                else -> _breakingNewsData.postValue(Resource.Error("Conversion Error!"))
            }
        }
    }

    private suspend fun safeSearchNewsRequest(query: String) {

        _searchNewsData.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = repositoryMain.getSearchNews(query, searchNewsPage)
                _searchNewsData.postValue(handleNewsResponse(response))
            }
            else {
                _searchNewsData.postValue(Resource.Error("No internet Connection"))
            }
        }
        catch (t : Throwable) {
            when(t) {
                is IOException -> _searchNewsData.postValue(Resource.Error("Network Failure!"))
                else -> _searchNewsData.postValue(Resource.Error("Conversion Error!"))
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = app.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }
        else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}