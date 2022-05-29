package com.randomusers.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class WeatherApiResponse {
    @SerializedName("data")
    @Expose
    val data: ArrayList<WeatherApiData> = ArrayList()

    @SerializedName("count")
    @Expose
    val count: Int = 0

    class WeatherApiData{
        @SerializedName("rh")
        @Expose
        val rh: String = ""

        @SerializedName("pod")
        @Expose
        val pod: String = ""

        @SerializedName("lon")
        @Expose
        val lon: String = ""

        @SerializedName("pres")
        @Expose
        val pres: String = ""

        @SerializedName("timezone")
        @Expose
        val timezone: String = ""

        @SerializedName("ob_time")
        @Expose
        val obTime: String = ""

        @SerializedName("country_code")
        @Expose
        val countryCode: String = ""

        @SerializedName("clouds")
        @Expose
        val clouds: String = ""

        @SerializedName("ts")
        @Expose
        val ts: String = ""

        @SerializedName("solar_rad")
        @Expose
        val solarRad: String = ""

        @SerializedName("state_code")
        @Expose
        val stateCode: String = ""

        @SerializedName("city_name")
        @Expose
        val cityName: String = ""

        @SerializedName("wind_spd")
        @Expose
        val windSpd: String = ""

        @SerializedName("wind_cdir_full")
        @Expose
        val windCdirFull: String = ""

        @SerializedName("wind_cdir")
        @Expose
        val windCdir: String = ""

        @SerializedName("slp")
        @Expose
        val slp: String = ""

        @SerializedName("vis")
        @Expose
        val vis: String = ""

        @SerializedName("h_angle")
        @Expose
        val hAngle: String = ""

        @SerializedName("sunset")
        @Expose
        val sunset: String = ""

        @SerializedName("dni")
        @Expose
        val dni: String = ""

        @SerializedName("dewpt")
        @Expose
        val dewpt: String = ""

        @SerializedName("snow")
        @Expose
        val snow: String = ""

        @SerializedName("uv")
        @Expose
        val uv: String = ""

        @SerializedName("precip")
        @Expose
        val precip: String = ""

        @SerializedName("wind_dir")
        @Expose
        val windDir: String = ""

        @SerializedName("sunrise")
        @Expose
        val sunrise: String = ""

        @SerializedName("ghi")
        @Expose
        val ghi: String = ""

        @SerializedName("dhi")
        @Expose
        val dhi: String = ""

        @SerializedName("aqi")
        @Expose
        val aqi: String = ""

        @SerializedName("lat")
        @Expose
        val lat: String = ""

        @SerializedName("weather")
        @Expose
        val weather: WeatherDetail = WeatherDetail()

        @SerializedName("datetime")
        @Expose
        val datetime: String = ""

        @SerializedName("temp")
        @Expose
        val temp: String = ""

        @SerializedName("station")
        @Expose
        val station: String = ""

        @SerializedName("elev_angle")
        @Expose
        val elevAngle: String = ""

        @SerializedName("app_temp")
        @Expose
        val appTemp: String = ""

    }

    class WeatherDetail{

        @SerializedName("icon")
        @Expose
        val icon: String = ""

        @SerializedName("code")
        @Expose
        val code: String = ""

        @SerializedName("description")
        @Expose
        val description: String = ""

    }
}