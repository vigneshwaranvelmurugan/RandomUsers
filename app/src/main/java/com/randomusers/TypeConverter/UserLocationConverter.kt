package com.randomusers.TypeConverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.randomusers.Model.RandomUserResponseResult

class UserLocationConverter {

    @TypeConverter
    fun fromSource(source: RandomUserResponseResult.RandomUserLocation?): String {
        var sourceValue:String=""
        if (source!=null){
            sourceValue=Gson().toJson(source)
        }
        return sourceValue
    }

    @TypeConverter
    fun toSource(source: String): RandomUserResponseResult.RandomUserLocation {
        var sourceData: RandomUserResponseResult.RandomUserLocation? = null
        if (source.isNotEmpty()) {
            sourceData = Gson().fromJson(source, RandomUserResponseResult.RandomUserLocation::class.java)
        }
        return sourceData!!
    }

}
