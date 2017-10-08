package zmuzik.cryptobserve.repo

import io.reactivex.Observable
import retrofit2.http.GET
import zmuzik.cryptobserve.repo.entities.CoinListResponse


interface CoinListApi {

    @GET("coinlist")
    fun coinlist(): Observable<CoinListResponse>
}