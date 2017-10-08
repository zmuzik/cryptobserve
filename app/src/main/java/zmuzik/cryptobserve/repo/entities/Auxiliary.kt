package zmuzik.cryptobserve.repo.entities


data class CoinListResponse(
        var response: String,
        var message: String,
        var baseImageUrl: String,
        var baseLinkUrl: String,
        var data: HashMap<String, Coin>
)

data class FavCoinListItem(
        var ticker: String,
        var name: String,
        var price: Double?
) {
    constructor() : this("", "", null)
}
