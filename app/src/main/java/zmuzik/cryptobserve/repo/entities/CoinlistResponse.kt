package zmuzik.cryptobserve.repo.entities


data class CoinlistResponse(
        var response: String,
        var message: String,
        var baseImageUrl: String,
        var baseLinkUrl: String,
        var data: HashMap<String, Coin>
)