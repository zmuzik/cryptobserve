package zmuzik.cryptobserve.repo.entities

import android.arch.persistence.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = arrayOf("ticker", "time"))
data class MinutePrice(
        var ticker: String,
        @SerializedName("time")
        var time: Long,
        @SerializedName("open")
        var open: Double,
        @SerializedName("high")
        var high: Double,
        @SerializedName("low")
        var low: Double,
        @SerializedName("close")
        var close: Double,
        @SerializedName("volumefrom")
        var volumeFrom: Double,
        @SerializedName("volumeto")
        var volumeTo: Double) {
    constructor() : this("", 0L, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
}