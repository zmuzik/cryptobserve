package zmuzik.cryptobserve.repo.daos

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import zmuzik.cryptobserve.repo.entities.Coin
import zmuzik.cryptobserve.repo.entities.FavCoinListItem
import zmuzik.cryptobserve.repo.entities.FavoriteCoin

@Dao
interface CoinDao {
    @Query("SELECT * FROM Coin order by sortOrder")
    fun getAll(): LiveData<List<Coin>>

    @Query("SELECT Coin.ticker as ticker, Coin.coinName as name, FavoriteCoin.currentPrice as price " +
            "FROM Coin, FavoriteCoin " +
            "WHERE Coin.ticker = FavoriteCoin.ticker " +
            "ORDER BY sortOrder")
    fun getAllFavorites(): LiveData<List<FavCoinListItem>>

    @Query("SELECT * FROM Coin where id = :arg0")
    fun getById(id: String): LiveData<Coin>

    @Query("SELECT * FROM Coin where id = :arg0")
    fun getByIdSync(id: String): Coin?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(device: Coin)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(devices: List<Coin>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favorite: FavoriteCoin)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorites(favorites: List<FavoriteCoin>)

    @Update
    fun update(device: Coin)

    @Delete
    fun delete(device: Coin)
}