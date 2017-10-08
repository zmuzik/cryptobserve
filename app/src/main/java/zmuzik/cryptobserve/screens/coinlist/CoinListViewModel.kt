package zmuzik.cryptobserve.screens.coinlist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import zmuzik.cryptobserve.repo.Repo
import zmuzik.cryptobserve.repo.entities.Coin
import javax.inject.Inject


class CoinListViewModel
@Inject constructor(val repo: Repo) : ViewModel() {

    fun getAllCoins(): LiveData<List<Coin>> = repo.getAllCoins()
}