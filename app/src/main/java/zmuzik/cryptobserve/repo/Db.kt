package zmuzik.cryptobserve.repo

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import zmuzik.cryptobserve.repo.daos.CoinDao
import zmuzik.cryptobserve.repo.daos.HistPriceDao
import zmuzik.cryptobserve.repo.entities.Coin
import zmuzik.cryptobserve.repo.entities.FavoriteCoin
import zmuzik.cryptobserve.repo.entities.MinutePrice


@Database(entities = arrayOf(
        Coin::class,
        FavoriteCoin::class,
        MinutePrice::class),
        version = 1)
abstract class Db : RoomDatabase() {
    abstract fun coinDao(): CoinDao

    abstract fun histPriceDao(): HistPriceDao
}