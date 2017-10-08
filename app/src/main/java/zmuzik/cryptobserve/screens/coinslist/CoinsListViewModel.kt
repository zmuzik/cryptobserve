package zmuzik.cryptobserve.screens.coinslist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import zmuzik.cryptobserve.repo.Repo
import zmuzik.cryptobserve.repo.entities.Coin
import javax.inject.Inject


class CoinsListViewModel
@Inject constructor(val repo: Repo) : ViewModel() {

    fun getAllCoins(): LiveData<List<Coin>> = repo.getAllCoins()
}