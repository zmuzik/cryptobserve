package zmuzik.cryptobserve.repo

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface PricingApi {

    @GET("pricemulti")
    fun favsPrices(@Query("fsyms") tickers: String, @Query("tsyms") currency: String):
            Observable<Map<String, Map<String, String>>>
}