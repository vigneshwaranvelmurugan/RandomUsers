package com.randomusers.viewModel

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.google.gson.Gson
import com.randomusers.Model.RandomUserDbResult
import com.randomusers.Model.RandomUserResponse
import com.randomusers.Model.WeatherApiResponse
import com.randomusers.R
import com.randomusers.Utils.ApiResponse
import com.randomusers.rest.RetrofitClient
import com.randomusers.rest.WeatherRetrofitClient
import com.randomusers.room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel(context: Context) : ViewModel() {
    var appDatabase: AppDatabase
    var randomUserData: MutableLiveData<RandomUserDbResult?>
    var userAddress = MutableLiveData("")
    var weatherLoaderEnable = MutableLiveData(View.GONE)
    var weatherDetailEnable = MutableLiveData(View.GONE)
    var weatherdetail: MutableLiveData<ApiResponse<ArrayList<WeatherApiResponse.WeatherApiData>>?>
    var callWeatherApi: Call<WeatherApiResponse>? = null
    var weatherMessage = MutableLiveData("")
    var weatherDegree = MutableLiveData("")

    init {
        appDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "random_user_db")
            .allowMainThreadQueries().build()
        randomUserData = MutableLiveData<RandomUserDbResult?>()
        weatherdetail =
            MutableLiveData<ApiResponse<ArrayList<WeatherApiResponse.WeatherApiData>>?>()
    }

    fun getRandomUserDetail(randomId: Int) {
        randomUserData.postValue(appDatabase.randomUserDao().getSelectedRandomUser(randomId))
    }

    fun getWeatherDetail(lon: String, lat: String, context: Context) {
        weatherdetail.value = ApiResponse.loading(data = null)

        val apiService  = WeatherRetrofitClient.apiInterface
        callWeatherApi = apiService. getWeatherDetail(
            lon, lat, context.resources.getString(
                R.string.weather_host_value
            ), context.resources.getString(R.string.weather_api_key)
        )
        callWeatherApi!!.enqueue(object : Callback<WeatherApiResponse> {
            override fun onFailure(call: Call<WeatherApiResponse>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<WeatherApiResponse>,
                response: Response<WeatherApiResponse>
            ) {

                weatherdetail.value = ApiResponse.success(data = response.body()!!.data)
            }
        })
    }

}