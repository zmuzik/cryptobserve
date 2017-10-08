package zmuzik.cryptobserve.repo.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Coin(
        @PrimaryKey
        var id: String,
        var url: String,
        var imageUrl: String,
        @SerializedName("Name")
        var ticker: String,
        var coinName: String,
        var fullName: String,
        var algorithm: String,
        var proofType: String,
        var sortOrder: String
) {
    constructor() : this("", "", "", "", "", "", "", "", "")
}