package zmuzik.cryptobserve.repo.entities


data class Coin(
        var id: String,
        var url: String,
        var imageUrl: String,
        var name: String,
        var coinName: String,
        var fullName: String,
        var algorithm: String,
        var proofType: String,
        var sortOrder: String
)