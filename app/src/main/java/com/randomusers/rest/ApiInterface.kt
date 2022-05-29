package com.randomusers.rest

import com.randomusers.Model.RandomUserResponse
import com.randomusers.Model.WeatherApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface ApiInterface {
    @GET("api/")
    fun getRandomUsersList(
        @Query("results") results: String,
        @Query("page") page: String
    ): Call<RandomUserResponse>

    @GET("current/")
    fun getWeatherDetail(
        @Query("lon") lon: String,
        @Query("lat") lat: String,
        @Header("X-RapidAPI-Host") rapidApiHost: String,
        @Header("X-RapidAPI-Key") rapidApiKey: String,
        ): Call<WeatherApiResponse>
}