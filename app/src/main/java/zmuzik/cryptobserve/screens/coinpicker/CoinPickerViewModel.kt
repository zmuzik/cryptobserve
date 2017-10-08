package zmuzik.cryptobserve.screens.coinpicker

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import zmuzik.cryptobserve.repo.Repo
import zmuzik.cryptobserve.repo.entities.Coin
import javax.inject.Inject


class CoinPickerViewModel
@Inject constructor(val repo: Repo) : ViewModel() {

    fun maybeRequestUpdate() = repo.maybeRequestCoinListUpdate()

    fun getAllCoins(): LiveData<List<Coin>> = repo.getAllCoins()

}