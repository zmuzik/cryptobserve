package zmuzik.cryptobserve.repo

import android.arch.lifecycle.LiveData
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import zmuzik.cryptobserve.repo.entities.Coin


class DefaultRepo(val db: Db, val api: Api) : Repo {

    override fun maybeRequestAllCoinsUpdate() {
        api.coinlist()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    db.coinDao().insertAll(result.data.values.toList())
                }, { error ->
                    Timber.e(error)
                })
    }

    override fun getAllCoins(): LiveData<List<Coin>> = db.coinDao().getAll()
}