package ir.ariyana.news_application_mvvm.utils

import androidx.room.TypeConverter
import ir.ariyana.news_application_mvvm.repository.model.Source

class Converters {

    @TypeConverter
    fun fromSource(source : Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name : String) : Source {
        return Source(name, name)
    }
}