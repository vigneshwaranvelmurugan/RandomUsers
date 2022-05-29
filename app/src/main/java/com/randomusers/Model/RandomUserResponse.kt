package com.randomusers.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RandomUserResponse {
    @SerializedName("results")
    @Expose
    val results: List<RandomUserResponseResult>? = null
}