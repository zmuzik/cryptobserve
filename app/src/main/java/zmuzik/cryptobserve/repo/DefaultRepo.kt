package zmuzik.cryptobserve.repo

import android.arch.lifecycle.LiveData
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import zmuzik.cryptobserve.Conf
import zmuzik.cryptobserve.Time
import zmuzik.cryptobserve.repo.entities.Coin
import zmuzik.cryptobserve.repo.entities.FavCoinListItem
import zmuzik.cryptobserve.repo.entities.FavoriteCoin


class DefaultRepo(val db: Db, val prefs: Prefs, val coinListApi: CoinListApi,
                  val pricingApi: PricingApi) : Repo {

    init {
        if (!prefs.isDbInitialized) {
            bg {
                db.coinDao().insertFavorite(FavoriteCoin("BTC", null))
                db.coinDao().insertFavorite(FavoriteCoin("ETH", null))
                prefs.isDbInitialized = true
            }
        }
    }

    private inline fun bg(crossinline func: () -> kotlin.Unit) {
        Observable.fromCallable { func() }.subscribeOn(Schedulers.io()).subscribe()
    }

    override fun maybeRequestCoinListUpdate() {
        if (prefs.lastCoinListUpdate + Conf.COIN_LIST_UPDATE_INTERVAL > Time.now) return

        coinListApi.coinlist()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    db.coinDao().insertAll(result.data.values.toList())
                    prefs.lastCoinListUpdate = Time.now
                }, { error ->
                    Timber.e(error)
                })
    }

    override fun maybeRequestFavPricesUpdate() {
        if (prefs.lastFavPricesUpdate + Conf.FAV_PRICES_UPDATE_INTERVAL > Time.now) return

        val tickers = "BTC,ETH"
        pricingApi.favsPrices(tickers, Conf.BASE_CURRENCY)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    db.coinDao().insertFavorites(result.flatMap {
                        listOf(FavoriteCoin(it.key, it.value[Conf.BASE_CURRENCY]?.toDouble()))
                    })
                    prefs.lastFavPricesUpdate = Time.now
                }, { error ->
                    Timber.e(error)
                })
    }

    override fun getAllCoins(): LiveData<List<Coin>> = db.coinDao().getAll()

    override fun getAllFavoriteCoins(): LiveData<List<FavCoinListItem>> = db.coinDao().getAllFavorites()
}