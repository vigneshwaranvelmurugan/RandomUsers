package com.randomusers.TypeConverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.randomusers.Model.RandomUserResponseResult

class UserPictureConverter {

    @TypeConverter
    fun fromSource(source: RandomUserResponseResult.UserPicture): String {
        return Gson().toJson(source)
    }

    @TypeConverter
    fun toSource(source: String): RandomUserResponseResult.UserPicture {
        var sourceData: RandomUserResponseResult.UserPicture? = null
        if (source.isNotEmpty()) {
            sourceData = Gson().fromJson(source, RandomUserResponseResult.UserPicture::class.java)
        }
        return sourceData!!
    }

}