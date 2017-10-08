package zmuzik.cryptobserve

import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide


fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun View.setVisible(visible: Boolean): Unit = this.setVisibility(if (visible) View.VISIBLE else View.GONE)

fun ImageView.loadImg(imageUrl: String) {
    if ("" == imageUrl) return
    Glide.with(context).load(imageUrl).centerCrop().into(this)
}

fun AppCompatActivity.toast(message : String) {
    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
}