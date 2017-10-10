package zmuzik.cryptobserve

import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import java.text.DecimalFormat
import java.text.SimpleDateFormat


fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun View.setVisible(visible: Boolean) = setVisibility(if (visible) View.VISIBLE else View.GONE)

fun ImageView.loadImg(imageUrl: String) {
    if ("" == imageUrl) return
    Glide.with(context).load(imageUrl).centerCrop().into(this)
}

fun AppCompatActivity.toast(message: String) {
    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
}

fun Double.format(): String {
    val df = DecimalFormat("0")
    df.maximumFractionDigits = 20
    return df.format(this)
}

fun Long.toHourAndMinute(): String = SimpleDateFormat("HH:mm").format(this)

fun Long.toDateTime(): String = SimpleDateFormat("yyyy-MM-dd HH:mm").format(this)

fun Long.toDateTimeShort(): String = SimpleDateFormat("MM-dd HH:mm").format(this)

fun Long.toYearMonthDay(): String = SimpleDateFormat("yyyy-MM-dd").format(this)

fun Long.toMonthDay(): String = SimpleDateFormat("MM-dd").format(this)