package ir.ariyana.news_application_mvvm.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.ariyana.news_application_mvvm.repository.RepositoryMain
import ir.ariyana.news_application_mvvm.repository.model.NewDataClass
import ir.ariyana.news_application_mvvm.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class ViewModelMain(private val repositoryMain: RepositoryMain) : ViewModel() {

    private val _breakingNewsData = MutableLiveData<Resource<NewDataClass>>()
    private val _searchNewsData = MutableLiveData<Resource<NewDataClass>>()
    private var breakingNewsPage = 1

    // use this technique to avoid changing data from ui controller
    val breakingNewsData : LiveData<Resource<NewDataClass>>
        get() = _breakingNewsData
    val searchNewsData : LiveData<Resource<NewDataClass>>
        get() = _searchNewsData

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
            val response = repositoryMain.getSearchNews(query, breakingNewsPage)
            _searchNewsData.postValue(handleNewsResponse(response))
        }
    }

    private fun handleNewsResponse(response : Response<NewDataClass>) : Resource<NewDataClass> {

        if(response.isSuccessful) {
            response.body()?.let { res ->
                return Resource.Success(res)
            }
        }
        return Resource.Error(response.message())
    }
}