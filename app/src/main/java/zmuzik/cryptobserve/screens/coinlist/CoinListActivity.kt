package zmuzik.cryptobserve.screens.coinlist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_coin_list.*
import kotlinx.android.synthetic.main.item_fav_coin_list.view.*
import zmuzik.cryptobserve.*
import zmuzik.cryptobserve.di.ViewModelFactory
import zmuzik.cryptobserve.repo.entities.FavCoinListItem
import zmuzik.cryptobserve.screens.coindetail.CoinDetailActivity
import zmuzik.cryptobserve.screens.coinpicker.CoinPickerActivity
import javax.inject.Inject

class CoinListActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: CoinListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_list)
        setSupportActionBar(toolbar)
        coinsListRv.layoutManager = LinearLayoutManager(this)

        fab.setOnClickListener { startActivity(Intent(this, CoinPickerActivity::class.java)) }

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CoinListViewModel::class.java)
        viewModel.getAllFavoriteCoins().observe(this, Observer { it?.let { onCoinsLoaded(it) } })
    }

    override fun onResume() {
        super.onResume()
        viewModel.maybeRequestUpdates()
    }

    private fun onCoinsLoaded(coins: List<FavCoinListItem>) {
        coinsListRv.setVisible(!coins.isEmpty())
        emptyListPlaceholder.setVisible(coins.isEmpty())
        if (coinsListRv.adapter == null) {
            coinsListRv.adapter = FavCoinListAdapter(coins)
        } else {
            (coinsListRv.adapter as FavCoinListAdapter).replaceData(coins)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_info -> {
                showInfoDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showInfoDialog() {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.info))
                .setMessage(getString(R.string.app_info_message))
                .setPositiveButton(getString(android.R.string.ok)) { dialog, _ -> dialog.dismiss() }
                .show()
    }

    private fun showDeleteDialog(ticker: String, name: String) {
        AlertDialog.Builder(this)
                .setMessage(getString(R.string.q_remove_x_from_fav, name))
                .setNegativeButton(getString(android.R.string.cancel)) { dialog, _ -> dialog.dismiss() }
                .setPositiveButton(getString(android.R.string.ok)) { _, _ -> viewModel.deleteFavCoin(ticker) }
                .show()
    }

    private fun openDetail(coin: FavCoinListItem) {
        val intent = Intent(this, CoinDetailActivity::class.java)
        intent.putExtra(Keys.COIN, coin)
        startActivity(intent)
    }


    inner class FavCoinListAdapter(var coins: List<FavCoinListItem>) : RecyclerView.Adapter<FavCoinListAdapter.ViewHolder>() {

        fun replaceData(newCoins: List<FavCoinListItem>) {
            coins = newCoins
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                ViewHolder(parent.inflate(R.layout.item_fav_coin_list))

        override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindItem(position)

        override fun getItemCount(): Int = coins.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            fun bindItem(position: Int) {
                val coin = coins[position]
                itemView.coinNameTv.text = coin.name
                itemView.coinRateTv.text = coin.price?.toString() ?: ""
                itemView.coinRateUnitTv.text = "${Conf.BASE_CURRENCY}/${coin.ticker}"
                itemView.coinLogoIv.loadImg(Conf.BASE_IMAGE_URL + coin.imageUrl)
                itemView.setOnLongClickListener { showDeleteDialog(coin.ticker, coin.name); true }
                itemView.setOnClickListener { openDetail(coin) }
            }
        }
    }
}
