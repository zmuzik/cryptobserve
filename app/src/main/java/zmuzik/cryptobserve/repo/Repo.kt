package zmuzik.cryptobserve.repo

import android.arch.lifecycle.LiveData
import zmuzik.cryptobserve.repo.entities.Coin


interface Repo {

    fun maybeRequestAllCoinsUpdate()

    fun getAllCoins(): LiveData<List<Coin>>

    fun getAllFavoriteCoins(): LiveData<List<Coin>>

}