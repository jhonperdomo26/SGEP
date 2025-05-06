package com.example.sgep.data.converters

import androidx.room.TypeConverter
import com.example.sgep.data.entity.EjercicioEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class EjercicioListConverter {

    @TypeConverter
    fun fromEjercicioList(ejercicios: List<EjercicioEntity>?): String {
        val gson = Gson()
        return gson.toJson(ejercicios)
    }

    @TypeConverter
    fun toEjercicioList(data: String?): List<EjercicioEntity>? {
        val gson = Gson()
        val type = object : TypeToken<List<EjercicioEntity>>() {}.type
        return gson.fromJson(data, type)
    }
}