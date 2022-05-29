package com.randomusers.TypeConverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.randomusers.Model.RandomUserResponseResult

class UserIdConverter {

    @TypeConverter
    fun fromSource(source: RandomUserResponseResult.UserDetailId): String {
        return Gson().toJson(source)
    }

    @TypeConverter
    fun toSource(source: String): RandomUserResponseResult.UserDetailId {
        var sourceData: RandomUserResponseResult.UserDetailId? = null
        if (source.isNotEmpty()) {
            sourceData = Gson().fromJson(source, RandomUserResponseResult.UserDetailId::class.java)
        }
        return sourceData!!
    }

}