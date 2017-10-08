package zmuzik.cryptobserve

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun View.setVisible(visible: Boolean): Unit = this.setVisibility(if (visible) View.VISIBLE else View.GONE)
