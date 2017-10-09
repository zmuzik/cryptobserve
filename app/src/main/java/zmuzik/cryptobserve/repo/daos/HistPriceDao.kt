package zmuzik.cryptobserve.repo.daos

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import zmuzik.cryptobserve.repo.entities.MinutePrice

@Dao
interface HistPriceDao {

    @Query("SELECT * FROM MinutePrice where ticker = :arg0 and time >= :arg1")
    fun getMinutePrices(ticker: String, from: Long): LiveData<List<MinutePrice>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMinutePrices(prices: List<MinutePrice>)
}