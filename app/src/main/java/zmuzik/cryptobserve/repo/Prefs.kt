package zmuzik.cryptobserve.repo

import android.content.SharedPreferences
import zmuzik.cryptobserve.Keys


class Prefs(val sp: SharedPreferences) {

    var isDbInitialized: Boolean
        get() = sp.getBoolean(Keys.IS_DB_INITIALIZED, false)
        set(value) = sp.edit().putBoolean(Keys.IS_DB_INITIALIZED, value).apply()

}