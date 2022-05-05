package ir.ariyana.news_application_mvvm.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.ariyana.news_application_mvvm.repository.RepositoryMain
import ir.ariyana.news_application_mvvm.repository.model.Article
import ir.ariyana.news_application_mvvm.repository.model.NewDataClass
import ir.ariyana.news_application_mvvm.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class ViewModelMain(private val repositoryMain: RepositoryMain) : ViewModel() {

    private val _breakingNewsData = MutableLiveData<Resource<NewDataClass>>()
    private val _searchNewsData = MutableLiveData<Resource<NewDataClass>>()

    private var breakingNewsPage = 1
    private var searchNewsPage = 1

    // use this technique to avoid changing data from ui controller
    val breakingNewsData : LiveData<Resource<NewDataClass>>
        get() = _breakingNewsData
    val searchNewsData : LiveData<Resource<NewDataClass>>
        get() = _searchNewsData

    var breakingNewsResponse : NewDataClass ? = null
    var searchNewsResponse : NewDataClass ? = null

    init {
        getBreakingNews("us")
    }

    private fun getBreakingNews(countryCode : String) {
        viewModelScope.launch {
            _breakingNewsData.postValue(Resource.Loading())
            val response = repositoryMain.getBreakingNews(countryCode, breakingNewsPage)
            _breakingNewsData.postValue(handleNewsResponse(response))
        }
    }

    fun getSearchedNews(query : String) {
        viewModelScope.launch {
            _searchNewsData.postValue(Resource.Loading())
            val response = repositoryMain.getSearchNews(query, searchNewsPage)
            _searchNewsData.postValue(handleSearchedResponse(response))
        }
    }

    fun insertArticle(article: Article) = viewModelScope.launch {
        repositoryMain.insertArticle(article)
    }

    fun removeArticle(article: Article) = viewModelScope.launch {
        repositoryMain.removeArticle(article)
    }

    fun receiveArticles() = repositoryMain.receiveArticles()

    private fun handleNewsResponse(response : Response<NewDataClass>) : Resource<NewDataClass> {

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

    private fun handleSearchedResponse(response : Response<NewDataClass>) : Resource<NewDataClass> {

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
}