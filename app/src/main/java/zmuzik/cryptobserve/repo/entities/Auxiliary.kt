package zmuzik.cryptobserve.repo.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class CoinListResponse(
        var response: String,
        var message: String,
        var baseImageUrl: String,
        var baseLinkUrl: String,
        var data: HashMap<String, Coin>
)

@Parcelize
data class FavCoinListItem(
        var id: String,
        var ticker: String,
        var name: String,
        var imageUrl: String,
        var price: Double?
) : Parcelable {
    constructor() : this("", "", "", "", null)
}
