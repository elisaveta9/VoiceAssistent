package com.example.voiceassistent.ai.geocoder

import com.example.voiceassistent.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodeApi {
    @GET("1.x/?apikey=${BuildConfig.GEOCODE_API_KEY}&format=json&results=1")
    fun getGeocode(@Query("geocode") geocode: String?): Call<Geocode?>?
}