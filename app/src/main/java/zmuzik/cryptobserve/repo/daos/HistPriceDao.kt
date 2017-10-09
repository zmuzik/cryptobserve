package zmuzik.cryptobserve.repo.daos

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import zmuzik.cryptobserve.repo.entities.HistPrice

@Dao
interface HistPriceDao {

    @Query("SELECT * FROM HistPrice where ticker = :arg0 and timeFrame = :arg1 and time >= :arg2")
    fun getHistPrices(ticker: String, timeFrame: String, from: Long): LiveData<List<HistPrice>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistPrices(prices: List<HistPrice>)
}