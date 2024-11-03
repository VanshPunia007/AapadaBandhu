package com.priyavansh.aapadabandhu.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.priyavansh.aapadabandhu.models.Source

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromSource(source: Source?): String? {
        return gson.toJson(source)  // Convert Source to JSON string
    }

    @TypeConverter
    fun toSource(sourceJson: String?): Source? {
        val type = object : TypeToken<Source>() {}.type
        return gson.fromJson(sourceJson, type)  // Convert JSON string back to Source
    }
}
