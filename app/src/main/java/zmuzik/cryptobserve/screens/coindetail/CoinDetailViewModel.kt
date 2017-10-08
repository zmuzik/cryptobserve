package zmuzik.cryptobserve.screens.coindetail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import zmuzik.cryptobserve.repo.Repo
import zmuzik.cryptobserve.repo.entities.Coin
import javax.inject.Inject


class CoinDetailViewModel
@Inject constructor(val repo: Repo) : ViewModel() {

    lateinit var coinId: String

    fun maybeRequestUpdate() {

    }

    fun getCoin(): LiveData<Coin> = repo.getCoin(coinId)

}