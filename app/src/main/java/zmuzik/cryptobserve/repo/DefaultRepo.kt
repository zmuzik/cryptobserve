package zmuzik.cryptobserve.repo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import zmuzik.cryptobserve.repo.entities.Coin


class DefaultRepo : Repo {

    override fun getAllCoins(): LiveData<List<Coin>> {
        return MutableLiveData()
    }

}