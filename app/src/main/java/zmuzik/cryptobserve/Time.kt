package zmuzik.cryptobserve


import java.util.*


object Time {

    val SECOND = 1000L
    val MINUTE = 60 * SECOND
    val HOUR = 60 * MINUTE
    val DAY = 24 * HOUR

    val today
        get() = GregorianCalendar()
                .apply { set(Calendar.HOUR_OF_DAY, 0) }
                .apply { set(Calendar.MINUTE, 0) }
                .apply { set(Calendar.SECOND, 0) }
                .apply { set(Calendar.MILLISECOND, 0) }.timeInMillis

    val yesterday = today - DAY // start of yesterday

    val tomorrow = today + DAY // start of tomorrow

    val now get() = System.currentTimeMillis()
}