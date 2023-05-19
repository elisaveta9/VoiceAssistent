package com.example.voiceassistent.ai.weather

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ForecastService() {
    fun getApi(): ForecastApi? {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ForecastApi::class.java)
    }
}