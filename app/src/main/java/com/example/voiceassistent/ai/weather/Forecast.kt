package com.example.voiceassistent.ai.weather

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Forecast : Serializable {
    @SerializedName("main")
    @Expose
    var main: Main? = null

    @SerializedName("weather")
    @Expose
    var weather: ArrayList<Weather?> = ArrayList()

    @SerializedName("name")
    @Expose
    var name: String = ""
}
class Main {
    @SerializedName("temp")
    @Expose
    var temp: Double? = null
}

class Weather {
    @SerializedName("description")
    @Expose
    var description: String? = null
}