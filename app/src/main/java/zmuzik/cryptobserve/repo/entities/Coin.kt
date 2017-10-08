package zmuzik.cryptobserve.repo.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Coin(
        @PrimaryKey
        var id: String,
        var url: String,
        var imageUrl: String,
        var name: String,
        var coinName: String,
        var fullName: String,
        var algorithm: String,
        var proofType: String,
        var sortOrder: String
) {
    constructor() : this("", "", "", "", "", "", "", "", "")
}