package com.example.sgep.data.converters

import androidx.room.TypeConverter
import java.util.Date

/**
 * Convierte automáticamente entre Date y Long (timestamp) para Room.
 */
class DateConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
