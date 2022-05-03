package ir.ariyana.news_application_mvvm.local

import android.content.Context
import androidx.room.*
import ir.ariyana.news_application_mvvm.model.Article
import ir.ariyana.news_application_mvvm.utils.Converters

@Database(entities = [Article::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DatabaseNews : RoomDatabase() {

    abstract fun getArticleDao() : ArticleDao

    companion object {

        @Volatile
        private var database : DatabaseNews? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = database ?: synchronized(LOCK) {
            database ?: createDatabase(context).also {
                database = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                DatabaseNews::class.java,
                "database"
            )
                .allowMainThreadQueries()
                .build()
    }
}