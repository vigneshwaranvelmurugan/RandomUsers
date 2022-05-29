package com.randomusers.TypeConverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.randomusers.Model.RandomUserResponseResult

class UserLoginConverter {

    @TypeConverter
    fun fromSource(source: RandomUserResponseResult.UserLogin): String {
        return Gson().toJson(source)
    }

    @TypeConverter
    fun toSource(source: String): RandomUserResponseResult.UserLogin {
        var sourceData: RandomUserResponseResult.UserLogin? = null
        if (source.isNotEmpty()) {
            sourceData = Gson().fromJson(source, RandomUserResponseResult.UserLogin::class.java)
        }
        return sourceData!!
    }

}