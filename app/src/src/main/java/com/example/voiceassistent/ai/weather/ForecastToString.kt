package com.example.voiceassistent.ai.weather

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.function.Consumer
import kotlin.math.roundToInt

class ForecastToString {
    fun getForecast(lat: Double?, lon: Double?, city: String?, currLanguage: String?,
                    correctAnswer: String, defaultAnswer: String, callback: Consumer<String>) {
        if (lat == null || lon == null) {
            callback.accept(defaultAnswer.replace("{City}", city!!))
            return
        }

        val api: ForecastApi? = ForecastService().getApi()
        val call: Call<Forecast?>? = api?.getCurrentWeather(
            lat, lon, currLanguage)

        call!!.enqueue(object : Callback<Forecast?> {
            override fun onResponse(call: Call<Forecast?>, response: Response<Forecast?>) {
                val result = response.body()

                if (result != null){
                    val temp = result.main?.temp?.minus(273.15)?.let { (it * 10).roundToInt() / 10.0 }
                    val answer: String =  correctAnswer
                        .replace("{City}", city!!)
                        .replace("{Temperature}", temp.toString())
                        .replace("{WeatherDescriptions}", result.weather[0]?.description!!)
                    callback.accept(correctStr(temp!!, answer, currLanguage))
                }
                else{
                    callback.accept(defaultAnswer.replace("{City}", city!!))
                }
            }

            override fun onFailure(call: Call<Forecast?>, t: Throwable) {
                t.message?.let { Log.w("WEATHER", it) }
            }})

    }

    private fun correctStr(temp: Double, result: String, currLanguage: String?): String{
        val tmp = temp.toInt()
        var correctAnswer = result
        if (currLanguage == "ru") {
            if (tmp % 10 == 1 && tmp % 100 != 11)
                correctAnswer = result.replace("градусов"," градус")
            else if (tmp % 10 in 2..4 && (tmp % 100 < 10 || tmp % 100 >= 20))
                correctAnswer = result.replace("градусов"," градуса")
            if (result.contains("деревня"))
                correctAnswer = result.replace(" деревня", "деревне").replace("городе", "")
            else if (result.contains("село"))
                correctAnswer = result.replace(" село", "селе").replace("городе", "")
            else if (result.contains("посёлок"))
                correctAnswer = result.replace(" посёлок", "посёлке").replace("городе", "")
            else if (result.contains(" город "))
                correctAnswer = result.replace(" город ", " ")
        }
        return correctAnswer
    }
}