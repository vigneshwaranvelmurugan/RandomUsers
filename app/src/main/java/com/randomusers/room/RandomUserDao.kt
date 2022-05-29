package com.randomusers.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.randomusers.Model.RandomUserDbResult

@Dao
interface RandomUserDao {

    @Insert
    fun insertRandomUser(randomUser: RandomUserDbResult?): Long

    @Query("SELECT * FROM random_user")
    fun getAllRandomUsers(): List<RandomUserDbResult>

    @Query("DELETE FROM random_user")
    fun deleteAllRandomUsers()

    @Query("SELECT * FROM random_user WHERE name_first_last LIKE '%' || :filterName || '%'")
    fun filterRandomUsers(filterName: String): List<RandomUserDbResult>

    @Query("SELECT * FROM random_user WHERE random_id IN (:randomUserId)")
    fun getSelectedRandomUser(randomUserId:Int): RandomUserDbResult
}
