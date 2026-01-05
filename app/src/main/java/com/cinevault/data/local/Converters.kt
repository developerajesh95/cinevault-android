package com.cinevault.data.local

import androidx.room.TypeConverter
import com.cinevault.data.local.entity.MovieCategory

class Converters {

    @TypeConverter
    fun fromCategory(category: MovieCategory): String {
        return category.name
    }

    @TypeConverter
    fun toCategory(value: String): MovieCategory {
        return MovieCategory.valueOf(value)
    }

}