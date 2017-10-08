package zmuzik.cryptobserve.repo

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import zmuzik.cryptobserve.repo.entities.Coin
import zmuzik.cryptobserve.repo.daos.CoinDao


@Database(entities = arrayOf(
        Coin::class),
        version = 1)
abstract class Db : RoomDatabase() {
    abstract fun coinDao(): CoinDao
}