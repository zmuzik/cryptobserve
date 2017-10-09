package zmuzik.cryptobserve.screens.coindetail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import zmuzik.cryptobserve.repo.Repo
import zmuzik.cryptobserve.repo.entities.Coin
import zmuzik.cryptobserve.repo.entities.HistPrice
import javax.inject.Inject


class CoinDetailViewModel
@Inject constructor(val repo: Repo) : ViewModel() {

    lateinit var coinId: String
    lateinit var ticker: String

    fun maybeRequestUpdate() {
        repo.maybeRequestPricesForToday(ticker)
    }

    fun getCoin(): LiveData<Coin> = repo.getCoin(coinId)

    fun getPricesForToday(): LiveData<List<HistPrice>> = repo.getPricesForToday(ticker)

}