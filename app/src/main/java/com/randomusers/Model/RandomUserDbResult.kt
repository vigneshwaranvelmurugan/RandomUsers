package com.randomusers.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "random_user")
data class RandomUserDbResult(
    @ColumnInfo(name = "gender") var gender: String?,
    @ColumnInfo(name = "name_type") var name_type: String?,
    @ColumnInfo(name = "name_first_last") var name_first_last: String?,
    @ColumnInfo(name = "location") var userLocation: RandomUserResponseResult.RandomUserLocation?,
    @ColumnInfo(name = "dob") var dob: RandomUserResponseResult.UserDate?,
    @ColumnInfo(name = "email") var email: String?,
    @ColumnInfo(name = "phone") var phone: String?,
    @ColumnInfo(name = "cell") var cell: String?,
    @ColumnInfo(name = "nat") var nat: String?,
    @ColumnInfo(name = "registered") var registered: RandomUserResponseResult.UserDate?,
    @ColumnInfo(name = "picture") var picture: RandomUserResponseResult.UserPicture?,
    @ColumnInfo(name = "id") var id: RandomUserResponseResult.UserDetailId?,
    @ColumnInfo(name = "login") var login: RandomUserResponseResult.UserLogin?,
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "random_id")
    var random_id: Int? = null
}