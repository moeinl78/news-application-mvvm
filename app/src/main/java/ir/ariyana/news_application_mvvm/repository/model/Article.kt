package ir.ariyana.news_application_mvvm.repository.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "table_article")
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null,
    val author : String,
    val content : String,
    val description : String,
    val publishedAt : String,
    val source : NewDataClass.Article.Source,
    val title : String,
    val url : String,
    val urlToImage : String
) : Serializable