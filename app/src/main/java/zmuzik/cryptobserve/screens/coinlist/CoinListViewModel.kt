package zmuzik.cryptobserve.screens.coinlist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import zmuzik.cryptobserve.repo.Repo
import zmuzik.cryptobserve.repo.entities.FavCoinListItem
import javax.inject.Inject


class CoinListViewModel
@Inject constructor(val repo: Repo) : ViewModel() {

    fun maybeRequestUpdates() {
        repo.maybeRequestCoinListUpdate()
        repo.maybeRequestFavPricesUpdate()
    }

    fun getAllFavoriteCoins(): LiveData<List<FavCoinListItem>> = repo.getAllFavoriteCoins()

}