package zmuzik.cryptobserve.repo.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class FavoriteCoin(
        @PrimaryKey
        var ticker: String
) {
    constructor() : this("")
}