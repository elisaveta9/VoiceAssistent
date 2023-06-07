package com.example.voiceassistent.ai.geocoder

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Geocode(): Serializable {
    @SerializedName("response")
    @Expose
    val response: Response? = null
}
class Response(): Serializable {
    @SerializedName("GeoObjectCollection")
    @Expose
    val geoObjectCollection: GeoObjectCollection? = null
}
class GeoObjectCollection(): Serializable {
    @SerializedName("featureMember")
    @Expose
    val geoObjects: ArrayList<FeatureMember?> = ArrayList()
}
class FeatureMember(): Serializable {
    @SerializedName("GeoObject")
    @Expose
    val geoObject: GeoObject? = null
}
class GeoObject(): Serializable {
    @SerializedName("Point")
    @Expose
    val point: Point? = null

    @SerializedName("name")
    @Expose
    val name: String? = ""
}
class Point(): Serializable {
    @SerializedName("pos")
    @Expose
    val pos: String? = ""
}