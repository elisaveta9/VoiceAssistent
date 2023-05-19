package com.example.voiceassistent.ai.geocoder

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodeApi {
    @GET("/api/1/geocode?key=fe513676-92e2-478a-af88-d410182ca03c")
    fun getGeocode(@Query("q") placename: String?): Call<Geocode?>?
}