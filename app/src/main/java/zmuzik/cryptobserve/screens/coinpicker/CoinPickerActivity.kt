package zmuzik.cryptobserve.screens.coinpicker

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_coin_list.*
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
                //val res = itemView.resources
                itemView.coinNameTv.text = coin.coinName
                itemView.tickerTv.text = coin.ticker
                //itemView.algorithmTv.text = res.getString(R.string.algorithm, coin.algorithm)
                //itemView.proofTypeTv.text = res.getString(R.string.proof_type, coin.proofType)
                itemView.coinLogoIv.loadImg(Conf.BASE_IMAGE_URL + coin.imageUrl)
                itemView.setOnClickListener {
                    viewModel.addCoinToFavorites(coin.ticker)
                    finish()
                }
            }
        }
    }
}