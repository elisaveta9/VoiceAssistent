package com.example.voiceassistent.ai.weather

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastApi {
    @GET("data/2.5/weather?appid=c24a791a7ec11a7e8c4f551b7b03ae80")
    fun getCurrentWeather(
        @Query("lat") lat: Double?,
        @Query("lon") lon: Double?,
        @Query("lang") lang: String?): Call<Forecast?>?
}