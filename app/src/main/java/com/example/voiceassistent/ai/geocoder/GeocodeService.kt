package com.example.voiceassistent.ai.geocoder

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GeocodeService {
    fun getApi(): GeocodeApi?{
        val retrofit = Retrofit.Builder()
            .baseUrl("https://graphhopper.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(GeocodeApi::class.java)
    }
}