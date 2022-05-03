package ir.ariyana.news_application_mvvm.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_article")
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null,
    val author : String,
    val content : String,
    val description : String,
    val publishedAt : String,
    val source : String,
    val title : String,
    val url : String,
    val urlToImage : String
)