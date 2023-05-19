package com.example.voiceassistent.ai.geocoder

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Geocode(): Serializable {
    @SerializedName("hits")
    @Expose
    val hits: List<Hit?>? = null
}
class Hit(): Serializable {
    @SerializedName("point")
    @Expose
    val point: Point? = null
}
class Point(): Serializable {
    @SerializedName("lat")
    @Expose
    val lat: Double? = null

    @SerializedName("lng")
    @Expose
    val lng: Double? = null
}