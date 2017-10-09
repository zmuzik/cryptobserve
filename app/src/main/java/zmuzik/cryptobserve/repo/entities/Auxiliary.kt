package zmuzik.cryptobserve.repo.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


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

data class CoinListResponse(
        var response: String,
        var message: String,
        var baseImageUrl: String,
        var baseLinkUrl: String,
        var data: HashMap<String, Coin>
)

data class HistPricesResponse(
        var response: String,
        var message: String,
        var type: Int,
        var aggregated: Boolean,
        var data: List<HistPrice>,
        var firstValueInArray: Boolean,
        var timeTo: Long,
        var timeFrom: Long
)
