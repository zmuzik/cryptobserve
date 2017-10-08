package zmuzik.cryptobserve.repo

import android.content.SharedPreferences
import zmuzik.cryptobserve.Keys


class Prefs(val sp: SharedPreferences) {

    var isDbInitialized: Boolean
        get() = sp.getBoolean(Keys.IS_DB_INITIALIZED, false)
        set(value) = sp.edit().putBoolean(Keys.IS_DB_INITIALIZED, value).apply()

    var lastCoinListUpdate: Long
        get() = sp.getLong(Keys.LAST_COIN_LIST_UPDATE, -1L)
        set(value) = sp.edit().putLong(Keys.LAST_COIN_LIST_UPDATE, value).apply()

    var lastFavPricesUpdate: Long
        get() = sp.getLong(Keys.LAST_FAV_PRICES_UPDATE, -1L)
        set(value) = sp.edit().putLong(Keys.LAST_FAV_PRICES_UPDATE, value).apply()

}