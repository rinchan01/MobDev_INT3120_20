package com.example.planegame

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class WeatherResponse(val current: Weather)

data class Weather(val condition: Condition)

data class Condition(val text: String, val code: Int)

const val MY_API_KEY = "0901c8f68093439f984150609240604"

interface WeatherAPI {

    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") key: String = MY_API_KEY,
        @Query("q") q: String,
        @Query("aqi") aqi: String = "no"
    ): Response<WeatherResponse>
}

object RetrofitInstance {
    val weatherAPI: WeatherAPI by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.weatherapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherAPI::class.java)
    }
}