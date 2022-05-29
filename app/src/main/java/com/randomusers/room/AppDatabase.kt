package com.randomusers.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.randomusers.Model.RandomUserDbResult
import com.randomusers.TypeConverter.*

@Database(entities = [(RandomUserDbResult::class)], version = 1)
@TypeConverters(value = [UserLocationConverter::class,UserDateConverter::class, UserPictureConverter::class, UserIdConverter::class, UserLoginConverter::class])

abstract class AppDatabase : RoomDatabase() {
    abstract fun randomUserDao(): RandomUserDao
}