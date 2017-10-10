package zmuzik.cryptobserve.screens.coinpicker

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import zmuzik.cryptobserve.repo.Repo
import zmuzik.cryptobserve.repo.entities.Coin
import java.lang.ref.WeakReference
import javax.inject.Inject


class CoinPickerViewModel
@Inject constructor(val repo: Repo) : ViewModel() {

    var filter: String = ""

    var coinsStream: WeakReference<LiveData<List<Coin>>>? = null

    fun maybeRequestUpdate() = repo.maybeRequestCoinListUpdate()

    fun getAllCoinsExFavorite(): LiveData<List<Coin>> {
        val stream = repo.getAllCoinsExFavorite(filter)
        coinsStream = WeakReference(stream)
        return stream
    }

    fun addCoinToFavorites(ticker: String) = repo.addCoinToFavorites(ticker)

    fun removePriceObservers(owner: LifecycleOwner) = coinsStream?.get()?.removeObservers(owner)

}