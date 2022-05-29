package com.randomusers.rest

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherRetrofitClient {
    val serverURL = "https://weatherbit-v1-mashape.p.rapidapi.com/"

    val retrofitClient: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(serverURL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    val apiInterface: ApiInterface by lazy {
        retrofitClient
            .build()
            .create(ApiInterface::class.java)
    }
}