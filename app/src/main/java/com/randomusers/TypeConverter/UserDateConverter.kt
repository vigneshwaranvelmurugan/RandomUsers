package com.randomusers.TypeConverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.randomusers.Model.RandomUserResponseResult

class UserDateConverter {

    @TypeConverter
    fun fromSource(source: RandomUserResponseResult.UserDate): String {
        return Gson().toJson(source)
    }

    @TypeConverter
    fun toSource(source: String): RandomUserResponseResult.UserDate {
        var sourceData: RandomUserResponseResult.UserDate? = null
        if (source.isNotEmpty()) {
            sourceData = Gson().fromJson(source, RandomUserResponseResult.UserDate::class.java)
        }
        return sourceData!!
    }

}