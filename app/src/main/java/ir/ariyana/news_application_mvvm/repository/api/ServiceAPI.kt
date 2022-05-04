package ir.ariyana.news_application_mvvm.repository.api

import ir.ariyana.news_application_mvvm.repository.model.NewDataClass
import ir.ariyana.news_application_mvvm.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceAPI {

    @GET("top-headlines")
    suspend fun getBreakingNews(
        @Query("country") countryCode : String = "us",
        @Query("page") page : Int = 1,
        @Query("apiKey") apiKey : String = Constants.API_KEY
    ) : Response<NewDataClass>

    @GET("everything")
    suspend fun getSearchedNews(
        @Query("q") query : String,
        @Query("page") page : Int = 1,
        @Query("apiKey") apiKey : String = Constants.API_KEY
    ) : Response<NewDataClass>
}