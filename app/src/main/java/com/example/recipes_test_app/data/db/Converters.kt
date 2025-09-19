package com.example.recipes_test_app.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromDishTypesList(list: List<String>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toDishTypesList(data: String): List<String> {
        if (data.isEmpty()) return emptyList()
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(data, listType)
    }
}
