package com.example.voiceassistent.ai.weather

import com.example.voiceassistent.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastApi {
    @GET("/data/2.5/weather?appid=${BuildConfig.WEATHER_API_KEY}")
    fun getCurrentWeather(
        @Query("lat") lat: Double?,
        @Query("lon") lon: Double?,
        @Query("lang") lang: String?): Call<Forecast?>?
}