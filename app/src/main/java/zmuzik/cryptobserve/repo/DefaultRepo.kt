package zmuzik.cryptobserve.repo

import android.arch.lifecycle.LiveData
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import zmuzik.cryptobserve.repo.entities.Coin
import zmuzik.cryptobserve.repo.entities.FavoriteCoin


class DefaultRepo(val db: Db, val prefs: Prefs, val api: Api) : Repo {

    init {
        if (!prefs.isDbInitialized) {
            bg {
                db.coinDao().inserFav(FavoriteCoin("BTC"))
                db.coinDao().inserFav(FavoriteCoin("ETH"))
                prefs.isDbInitialized = true
            }
        }
    }

    private inline fun bg(crossinline func: () -> kotlin.Unit) {
        Observable.fromCallable { func() }.subscribeOn(Schedulers.io()).subscribe()
    }

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

    override fun getAllFavoriteCoins(): LiveData<List<Coin>> = db.coinDao().getAllFavories()
}