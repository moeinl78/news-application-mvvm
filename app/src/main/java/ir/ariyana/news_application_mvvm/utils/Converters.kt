package ir.ariyana.news_application_mvvm.utils

import androidx.room.TypeConverter
import ir.ariyana.news_application_mvvm.model.NewDataClass

class Converters {

    @TypeConverter
    fun fromSource(source : NewDataClass.Article.Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name : String) : NewDataClass.Article.Source {
        return NewDataClass.Article.Source(name, name)
    }
}