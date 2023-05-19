package com.example.voiceassistent.ai.geocoder

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.function.Consumer

class GeocodeLocale {
    fun getGeocode(city: String?, callback: Consumer<Point>) {
        val api: GeocodeApi? = GeocodeService().getApi()
        val call: Call<Geocode?>? = api!!.getGeocode(city)

        call!!.enqueue((object : Callback<Geocode?>{
            override fun onResponse(call: Call<Geocode?>, response: Response<Geocode?>) {
                val result = response?.body()

                if (result != null) {
                    result.hits?.get(0)?.let { it.point?.let { it1 -> callback.accept(it1) } }
                }
            }

            override fun onFailure(call: Call<Geocode?>, t: Throwable) {
                t.message?.let { Log.w("GEOCODE", it) }
            }

        }))
    }
}