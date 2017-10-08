package zmuzik.cryptobserve.repo

import android.arch.lifecycle.LiveData
import zmuzik.cryptobserve.repo.entities.Coin


interface Repo {
    fun getAllCoins(): LiveData<List<Coin>>
}