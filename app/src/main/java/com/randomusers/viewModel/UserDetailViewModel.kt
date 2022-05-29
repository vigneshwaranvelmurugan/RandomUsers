package com.randomusers.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.randomusers.Model.RandomUserDbResult
import com.randomusers.room.AppDatabase

class UserDetailViewModel(context: Context) : ViewModel() {
    var appDatabase: AppDatabase
    var randomUserData: MutableLiveData<RandomUserDbResult?>
    var userDob = MutableLiveData("")
    var userAddress = MutableLiveData("")

    init {
        appDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "random_user_db")
            .allowMainThreadQueries().build()
        randomUserData=MutableLiveData<RandomUserDbResult?>()
    }

    fun getRandomUserDetail(randomId:Int){
        randomUserData.postValue(appDatabase.randomUserDao().getSelectedRandomUser(randomId))
    }
}