package net.thebookofcode.www.list_panelattempt.room.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import java.util.*

class DateConverter {
    @TypeConverter
    fun dateToJson(date: Date?): String {
        return Gson().toJson(date)
    }

    @TypeConverter
    fun jsonToDate(json: String?): Date {
        return Gson().fromJson(json, Date::class.java)
    }
}