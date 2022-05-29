package com.randomusers.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class RandomUserResponseResult {
    @SerializedName("gender")
    @Expose
    val gender: String = ""

    @SerializedName("name")
    @Expose
    val name: RandomUserNameDetail = RandomUserNameDetail()

    @SerializedName("location")
    @Expose
    val location: RandomUserLocation = RandomUserLocation()

    @SerializedName("email")
    @Expose
    val email: String = ""

    @SerializedName("login")
    @Expose
    val login: UserLogin? = null

    @SerializedName("dob")
    @Expose
    val dob: UserDate = UserDate()

    @SerializedName("registered")
    @Expose
    val registered: UserDate = UserDate()

    @SerializedName("phone")
    @Expose
    val phone: String = ""

    @SerializedName("cell")
    @Expose
    val cell: String = ""

    @SerializedName("id")
    @Expose
    val id: UserDetailId = UserDetailId()

    @SerializedName("picture")
    @Expose
    val picture: UserPicture = UserPicture()

    @SerializedName("nat")
    @Expose
    val nat: String = ""

    inner class RandomUserNameDetail {
        @SerializedName("title")
        @Expose
        val title: String = ""

        @SerializedName("first")
        @Expose
        val first: String = ""

        @SerializedName("last")
        @Expose
        val last: String = ""
    }

    inner class RandomUserLocation() {

        @SerializedName("street")
        @Expose
        val street: UserLocationStreet = UserLocationStreet()

        @SerializedName("city")
        @Expose
        val city: String = ""

        @SerializedName("state")
        @Expose
        val state: String = ""

        @SerializedName("country")
        @Expose
        val country: String = ""

        @SerializedName("postcode")
        @Expose
        val postcode: String = ""

        @SerializedName("coordinates")
        @Expose
        val coordinates: UserLocationCoordinates = UserLocationCoordinates()

        @SerializedName("timezone")
        @Expose
        val timezone: UserLocationTimezone = UserLocationTimezone()
    }

    class UserLocationStreet {
        @SerializedName("number")
        @Expose
        val number: String = ""

        @SerializedName("name")
        @Expose
        val name: String = ""
    }

    class UserLocationCoordinates{
        @SerializedName("latitude")
        @Expose
        val latitude: String = ""

        @SerializedName("longitude")
        @Expose
        val longitude: String = ""
    }

    class UserLocationTimezone{
        @SerializedName("offset")
        @Expose
        val offset: String = ""

        @SerializedName("description")
        @Expose
        val description: String = ""
    }

    class UserLogin{
        @SerializedName("uuid")
        @Expose
        val uuid: String = ""

        @SerializedName("username")
        @Expose
        val username: String = ""

        @SerializedName("password")
        @Expose
        val password: String = ""

        @SerializedName("salt")
        @Expose
        val salt: String = ""

        @SerializedName("md5")
        @Expose
        val md5: String = ""

        @SerializedName("sha1")
        @Expose
        val sha1: String = ""

        @SerializedName("sha256")
        @Expose
        val sha256: String = ""
    }

    class UserDate{
        @SerializedName("date")
        @Expose
        val date: String = ""

        @SerializedName("age")
        @Expose
        val age: String=""
    }

    class UserDetailId{
        @SerializedName("name")
        @Expose
        val name: String = ""

        @SerializedName("value")
        @Expose
        val value: String = ""
    }

    class UserPicture{
        @SerializedName("large")
        @Expose
        val large: String = ""

        @SerializedName("medium")
        @Expose
        val medium: String = ""

        @SerializedName("thumbnail")
        @Expose
        val thumbnail: String = ""
    }
}