package zmuzik.cryptobserve.repo

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import zmuzik.cryptobserve.repo.entities.HistPricesResponse

interface PricingApi {

    @GET("pricemulti")
    fun favsPrices(@Query("fsyms") tickers: String, @Query("tsyms") currency: String):
            Observable<Map<String, Map<String, String>>>

    // for 1h chart
    @GET("histominute?limit=60")
    fun histPrices1min(@Query("fsym") ticker: String, @Query("tsym") currency: String):
            Observable<HistPricesResponse>

    // for 1d chart
    @GET("histominute?limit=288&aggregate=5")
    fun histPrices5min(@Query("fsym") ticker: String, @Query("tsym") currency: String):
            Observable<HistPricesResponse>

    // for 1w chart
    @GET("histohour?limit=168")
    fun histPrices1hour(@Query("fsym") ticker: String, @Query("tsym") currency: String):
            Observable<HistPricesResponse>

    // for all other charts
    @GET("histoday?limit=366")
    fun histPrices1day(@Query("fsym") ticker: String, @Query("tsym") currency: String):
            Observable<HistPricesResponse>
}