package ir.ariyana.news_application_mvvm.repository.model

data class News(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)