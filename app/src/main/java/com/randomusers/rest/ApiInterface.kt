package com.randomusers.rest

import com.randomusers.Model.RandomUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {
    @GET("api/")
    fun getRandomUsersList(@Query("results") results:String,@Query("page") page:String) : Call<RandomUserResponse>
}