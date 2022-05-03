package ir.ariyana.news_application_mvvm.repository

import ir.ariyana.news_application_mvvm.repository.api.ManagerAPI
import ir.ariyana.news_application_mvvm.repository.local.DatabaseNews

class RepositoryMain(private val db : DatabaseNews) {

    suspend fun getBreakingNews(countryCode : String, pageNumber : Int) =
        ManagerAPI.api.getBreakingNews(countryCode, pageNumber)

    suspend fun getSearchNews(query : String) =
        ManagerAPI.api.getSearchedNews(query)
}