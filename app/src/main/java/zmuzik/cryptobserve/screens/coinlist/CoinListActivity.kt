package zmuzik.cryptobserve.screens.coinlist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_coins_list.*
import kotlinx.android.synthetic.main.item_coin_list.view.*
import zmuzik.cryptobserve.R
import zmuzik.cryptobserve.di.ViewModelFactory
import zmuzik.cryptobserve.inflate
import zmuzik.cryptobserve.repo.entities.Coin
import zmuzik.cryptobserve.setVisible
import javax.inject.Inject

class CoinListActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: CoinListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coins_list)
        setSupportActionBar(toolbar)

        coinsListRv.layoutManager = LinearLayoutManager(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CoinListViewModel::class.java)
        viewModel.getAllFavoriteCoins().observe(this, Observer { it?.let { onCoinsLoaded(it) } })
    }

    override fun onResume() {
        super.onResume()
        viewModel.maybeRequestAllCoinsUpdate()
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

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    inner class CoinListAdapter(var coins: List<Coin>) : RecyclerView.Adapter<CoinListAdapter.ViewHolder>() {

        fun replaceData(newNotes: List<Coin>) {
            coins = newNotes
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                ViewHolder(parent.inflate(R.layout.item_coin_list))

        override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindItem(position)

        override fun getItemCount(): Int = coins.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            fun bindItem(position: Int) {
                val coin = coins[position]
                itemView.coinNameTv.text = coin.ticker
            }
        }
    }
}
