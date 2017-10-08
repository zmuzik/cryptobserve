package zmuzik.cryptobserve.screens.coindetail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_coin_detail.*
import kotlinx.android.synthetic.main.activity_coin_list.*
import zmuzik.cryptobserve.*
import zmuzik.cryptobserve.di.ViewModelFactory
import zmuzik.cryptobserve.repo.entities.Coin
import zmuzik.cryptobserve.repo.entities.FavCoinListItem
import javax.inject.Inject


class CoinDetailActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: CoinDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_detail)

        if (intent == null || !intent.hasExtra(Keys.COIN)
                || intent.getParcelableExtra<FavCoinListItem>(Keys.COIN) == null) {
            toast(getString(R.string.unable_to_load_screen))
            finish()
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CoinDetailViewModel::class.java)

        intent.getParcelableExtra<FavCoinListItem>(Keys.COIN)?.let { viewModel.coinId = it.id }
        viewModel.getCoin().observe(this, Observer { it?.let { onCoinLoaded(it) } })
    }

    override fun onResume() {
        super.onResume()
        viewModel.maybeRequestUpdate()
    }

    fun onCoinLoaded(coin: Coin) {
        coinLogoIv.loadImg(Conf.BASE_IMAGE_URL + coin.imageUrl)
        coinNameTv.text = coin.fullName
    }
}