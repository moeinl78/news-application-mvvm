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
    private val _progressBar = MutableLiveData<Resource<NewDataClass>>()

    val progressBar : LiveData<Resource<NewDataClass>>
        get() = _progressBar

    val breakingNewsData : LiveData<Resource<NewDataClass>>
        get() = _breakingNewsData

    var breakingNewsPage = 1

    fun getBreakingNews(countryCode : String) {
        viewModelScope.launch {
            _progressBar.postValue(Resource.Loading())
            val response = repositoryMain.getBreakingNews(countryCode, breakingNewsPage)
            handleBreakingNewsResponse(response)
        }
    }

    private fun handleBreakingNewsResponse(response : Response<NewDataClass>) : Resource<NewDataClass> {

        if(response.isSuccessful) {
            response.body()?.let { res ->
                return Resource.Success(res)
            }
        }
        return Resource.Error(response.message())
    }
}