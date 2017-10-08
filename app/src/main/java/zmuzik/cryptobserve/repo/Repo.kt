package zmuzik.cryptobserve.repo

import android.arch.lifecycle.LiveData
import zmuzik.cryptobserve.repo.entities.Coin
import zmuzik.cryptobserve.repo.entities.FavCoinListItem


interface Repo {

    fun maybeRequestCoinListUpdate()

    fun maybeRequestFavPricesUpdate()

    fun getAllCoins(): LiveData<List<Coin>>

    fun getAllFavoriteCoins(): LiveData<List<FavCoinListItem>>

}