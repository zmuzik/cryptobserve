package zmuzik.cryptobserve.repo

import io.reactivex.Observable
import retrofit2.http.GET
import zmuzik.cryptobserve.repo.entities.CoinlistResponse


interface Api {

    @GET("coinlist")
    fun coinlist(): Observable<CoinlistResponse>
}