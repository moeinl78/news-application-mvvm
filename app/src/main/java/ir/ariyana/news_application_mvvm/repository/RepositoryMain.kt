package ir.ariyana.news_application_mvvm.repository

import ir.ariyana.news_application_mvvm.repository.api.ManagerAPI
import ir.ariyana.news_application_mvvm.repository.local.DatabaseNews
import ir.ariyana.news_application_mvvm.repository.model.Article

class RepositoryMain(private val db : DatabaseNews) {

    suspend fun getBreakingNews(countryCode : String, pageNumber : Int) =
        ManagerAPI.api.getBreakingNews(countryCode, pageNumber)

    suspend fun getSearchNews(query : String, pageNumber: Int) =
        ManagerAPI.api.getSearchedNews(query, pageNumber)

    suspend fun insertArticle(article : Article) =
        db.getArticleDao().insertArticle(article)

    suspend fun removeArticle(article: Article) =
        db.getArticleDao().removeArticle(article)

    fun receiveArticles() =
        db.getArticleDao().receiveArticles()
}