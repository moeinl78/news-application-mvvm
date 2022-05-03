package ir.ariyana.news_application_mvvm.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import ir.ariyana.news_application_mvvm.model.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertArticle(article : Article)

    @Delete
    suspend fun removeArticle(article : Article)

    @Query("SELECT * FROM table_article")
    fun receiveArticles() : LiveData<List<Article>>
}