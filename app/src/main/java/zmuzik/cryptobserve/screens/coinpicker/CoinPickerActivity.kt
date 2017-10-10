package zmuzik.cryptobserve.screens.coinpicker

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_coin_picker.*
import kotlinx.android.synthetic.main.item_coin_list.view.*
import zmuzik.cryptobserve.*
import zmuzik.cryptobserve.di.ViewModelFactory
import zmuzik.cryptobserve.repo.entities.Coin
import javax.inject.Inject


class CoinPickerActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: CoinPickerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_picker)
        coinsListRv.layoutManager = LinearLayoutManager(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CoinPickerViewModel::class.java)
        filterEt.setText(viewModel.filter)
        filterEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.filter = text.toString()
                viewModel.removePriceObservers(this@CoinPickerActivity)
                viewModel.getAllCoinsExFavorite().observe(this@CoinPickerActivity,
                        Observer { it?.let { onCoinsLoaded(it) } })
            }
        })
        viewModel.getAllCoinsExFavorite().observe(this, Observer { it?.let { onCoinsLoaded(it) } })
    }

    override fun onResume() {
        super.onResume()
        viewModel.maybeRequestUpdate()
    }

    private fun onCoinsLoaded(coins: List<Coin>) {
        coinsListRv.setVisible(!coins.isEmpty())
        emptyListPlaceholder.setVisible(coins.isEmpty())
        if (coinsListRv.adapter == null) {
            coinsListRv.adapter = CoinListAdapter(coins)
        } else {
            (coinsListRv.adapter as CoinListAdapter).replaceData(coins)
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        // force all edittexts to lose focus on touch elsewhere within the activity
        if (event.action == MotionEvent.ACTION_DOWN) {
            val focusedView = currentFocus
            if (focusedView is EditText) {
                val focusedViewRect = Rect()
                focusedView.getGlobalVisibleRect(focusedViewRect)
                if (!focusedViewRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    focusedView.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(focusedView.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    inner class CoinListAdapter(var coins: List<Coin>) : RecyclerView.Adapter<CoinListAdapter.ViewHolder>() {

        fun replaceData(newCoins: List<Coin>) {
            coins = newCoins
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                ViewHolder(parent.inflate(R.layout.item_coin_list))

        override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindItem(position)

        override fun getItemCount(): Int = coins.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            fun bindItem(position: Int) {
                val coin = coins[position]
                itemView.coinNameTv.text = coin.coinName
                itemView.tickerTv.text = coin.ticker
                itemView.coinLogoIv.loadImg(Conf.BASE_IMAGE_URL + coin.imageUrl)
                itemView.setOnClickListener {
                    viewModel.addCoinToFavorites(coin.ticker)
                    finish()
                }
            }
        }
    }
}