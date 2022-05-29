package com.randomusers.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.randomusers.Model.RandomUserDbResult
import com.randomusers.Model.RandomUserResponse
import com.randomusers.Utils.ApiResponse
import com.randomusers.rest.RetrofitClient
import com.randomusers.room.AppDatabase
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(context: Context) : ViewModel() {

    var appDatabase: AppDatabase
    var randomUserList: MutableLiveData<ApiResponse<List<RandomUserDbResult>?>>
    var randomUserSearchList: MutableLiveData<List<RandomUserDbResult>?>

    init {
        appDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "random_user_db")
            .allowMainThreadQueries().build()
        appDatabase.randomUserDao().deleteAllRandomUsers()
        randomUserList = MutableLiveData()
        randomUserSearchList = MutableLiveData()
    }


    fun getRandomData(pageNo: String) {
        randomUserList.value = ApiResponse.loading(data = null)
        val call = RetrofitClient.apiInterface.getRandomUsersList("25", pageNo)

        call.enqueue(object : Callback<RandomUserResponse> {
            override fun onFailure(call: Call<RandomUserResponse>, t: Throwable) {
                randomUserList.value = ApiResponse.error(data = null,"")
            }

            override fun onResponse(
                call: Call<RandomUserResponse>,
                response: Response<RandomUserResponse>
            ) {
                val randomListData: ArrayList<RandomUserDbResult> = ArrayList()
                GlobalScope.launch {
                    withContext(Dispatchers.Main) {
                        response.body()?.results!!.forEach { randomUserData ->
                            val randomUserValue = RandomUserDbResult(
                                randomUserData.gender,
                                randomUserData.name.title,
                                randomUserData.name.first + " " + randomUserData.name.last,
                                randomUserData.location,
                                randomUserData.dob,
                                randomUserData.email,
                                randomUserData.phone,
                                randomUserData.cell,
                                randomUserData.nat,
                                randomUserData.registered,
                                randomUserData.picture,
                                randomUserData.id,
                                randomUserData.login
                            )
                            val rowId=appDatabase.randomUserDao().insertRandomUser(
                                randomUserValue
                            ).toLong()
                            randomUserValue.random_id=rowId.toInt()
                            randomListData.add(randomUserValue)
                        }
                        randomUserList.value = ApiResponse.success(randomListData)
                    }
                }
            }
        })
    }

    fun filterRandomUser(searchName: String) {
        randomUserSearchList.value = appDatabase.randomUserDao().filterRandomUsers(searchName)
    }
}