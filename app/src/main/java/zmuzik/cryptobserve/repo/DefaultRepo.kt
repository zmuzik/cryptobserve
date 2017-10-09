package zmuzik.cryptobserve.repo

import android.arch.lifecycle.LiveData
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import zmuzik.cryptobserve.Conf
import zmuzik.cryptobserve.Time
import zmuzik.cryptobserve.repo.entities.*


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

    override fun maybeRequestFavPricesUpdate(force: Boolean) {
        if (prefs.lastFavPricesUpdate + Conf.FAV_PRICES_UPDATE_INTERVAL > Time.now && !force) return

        bg {
            val tickers = db.coinDao().getFavoriteCoinsSync().joinToString(separator = ",") { it.ticker }
            if (!tickers.isEmpty()) {
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
        }
    }

    override fun getAllCoins(): LiveData<List<Coin>> = db.coinDao().getAll()

    override fun getAllCoinsExFavorite(): LiveData<List<Coin>> = db.coinDao().getAllExFavorite()

    override fun getAllFavoriteCoins(): LiveData<List<FavCoinListItem>> = db.coinDao().getAllFavorites()

    override fun addCoinToFavorites(ticker: String) {
        bg {
            db.coinDao().insertFavorite(FavoriteCoin(ticker, null))
            maybeRequestFavPricesUpdate(true)
        }
    }

    override fun deleteFavCoin(ticker: String) = bg { db.coinDao().deleteFavCoin(ticker) }

    override fun getCoin(id: String): LiveData<Coin> = db.coinDao().getById(id)

    override fun getHistPrices(ticker: String, timeframe: Timeframe): LiveData<List<HistPrice>> {
        val from: Long = Time.now - when (timeframe) {
            Timeframe.HOUR -> Time.HOUR
            Timeframe.DAY -> Time.DAY
            Timeframe.WEEK -> Time.WEEK
            Timeframe.MONTH -> Time.MONTH
            Timeframe.YEAR -> Time.YEAR
        }
        return db.histPriceDao().getHistPrices(ticker, timeframe.name, from)
    }

    override fun requestHistPrices(ticker: String, timeframe: Timeframe) {
        val observable = when (timeframe) {
            Timeframe.HOUR -> pricingApi.histPrices1min(ticker, Conf.BASE_CURRENCY)
            Timeframe.DAY -> pricingApi.histPrices5min(ticker, Conf.BASE_CURRENCY)
            Timeframe.WEEK -> pricingApi.histPrices1hour(ticker, Conf.BASE_CURRENCY)
            else -> pricingApi.histPrices1day(ticker, Conf.BASE_CURRENCY)
        }

        observable.observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val data: List<HistPrice> = result.data
                    data.forEach {
                        it.ticker = ticker
                        it.time = it.time * 1000L
                        it.timeFrame = timeframe.name
                    }
                    db.histPriceDao().insertHistPrices(data)
                }, { error ->
                    Timber.e(error)
                })
    }
}