package zmuzik.cryptobserve.repo

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import zmuzik.cryptobserve.repo.entities.MinutePricesResponse

interface PricingApi {

    @GET("pricemulti")
    fun favsPrices(@Query("fsyms") tickers: String, @Query("tsyms") currency: String):
            Observable<Map<String, Map<String, String>>>

    @GET("histominute?limit=288&aggregate=5")
    fun minutePrices(@Query("fsym") ticker: String, @Query("tsym") currency: String):
            Observable<MinutePricesResponse>

    @GET("histohour")
    fun hourlyPrices(@Query("fsym") ticker: String, @Query("tsym") currency: String):
            Observable<Map<String, Map<String, String>>>

    @GET("histoday")
    fun dailyPrices(@Query("fsym") ticker: String, @Query("tsym") currency: String):
            Observable<Map<String, Map<String, String>>>
}