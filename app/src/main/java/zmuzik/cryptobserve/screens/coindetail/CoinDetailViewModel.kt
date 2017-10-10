package zmuzik.cryptobserve.screens.coindetail

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import zmuzik.cryptobserve.repo.Repo
import zmuzik.cryptobserve.repo.entities.Coin
import zmuzik.cryptobserve.repo.entities.HistPrice
import zmuzik.cryptobserve.repo.entities.Timeframe
import java.lang.ref.WeakReference
import javax.inject.Inject


class CoinDetailViewModel
@Inject constructor(val repo: Repo) : ViewModel() {

    lateinit var coinId: String
    lateinit var ticker: String
    var timeframe = Timeframe.HOUR
    var pricesStreamWr: WeakReference<LiveData<List<HistPrice>>>? = null

    fun requestUpdate() = repo.requestHistPrices(ticker, timeframe)

    fun getCoin(): LiveData<Coin> = repo.getCoin(coinId)

    fun removePriceObservers(owner: LifecycleOwner) = pricesStreamWr?.get()?.removeObservers(owner)

    fun getHistPrices(): LiveData<List<HistPrice>> {
        val pricesStream = repo.getHistPrices(ticker, timeframe)
        pricesStreamWr = WeakReference(pricesStream)
        return pricesStream
    }
}