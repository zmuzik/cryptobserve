package zmuzik.cryptobserve.repo

import android.arch.lifecycle.LiveData
import zmuzik.cryptobserve.repo.entities.Coin
import zmuzik.cryptobserve.repo.entities.FavCoinListItem
import zmuzik.cryptobserve.repo.entities.HistPrice
import zmuzik.cryptobserve.repo.entities.Timeframe


interface Repo {

    fun maybeRequestCoinListUpdate()

    fun maybeRequestFavPricesUpdate(force: Boolean)

    fun getAllCoins(): LiveData<List<Coin>>

    fun getAllCoinsExFavorite(filter: String): LiveData<List<Coin>>

    fun getAllFavoriteCoins(): LiveData<List<FavCoinListItem>>

    fun addCoinToFavorites(ticker: String)

    fun deleteFavCoin(ticker: String)

    fun getCoin(id : String): LiveData<Coin>

    fun getHistPrices(ticker: String, timeframe: Timeframe): LiveData<List<HistPrice>>

    fun requestHistPrices(ticker: String, timeframe: Timeframe)
}