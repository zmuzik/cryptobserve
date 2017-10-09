package zmuzik.cryptobserve.screens.coindetail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_coin_detail.*
import zmuzik.cryptobserve.*
import zmuzik.cryptobserve.di.ViewModelFactory
import zmuzik.cryptobserve.repo.entities.Coin
import zmuzik.cryptobserve.repo.entities.FavCoinListItem
import zmuzik.cryptobserve.repo.entities.MinutePrice
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

        intent.getParcelableExtra<FavCoinListItem>(Keys.COIN)?.let {
            viewModel.coinId = it.id
            viewModel.ticker = it.ticker
        }
        viewModel.getCoin().observe(this, Observer { it?.let { onCoinLoaded(it) } })
        viewModel.getPricesForToday().observe(this, Observer { it?.let { onTodayPricesLoaded(it) } })
    }

    override fun onResume() {
        super.onResume()
        viewModel.maybeRequestUpdate()
    }

    private fun onTodayPricesLoaded(prices: List<MinutePrice>) {
        if (prices.isEmpty()) return

        val yValues = ArrayList<CandleEntry>()
        prices.forEachIndexed { index, minutePrice ->
            yValues.add(CandleEntry(index.toFloat(), minutePrice.high.toFloat(),
                    minutePrice.low.toFloat(), minutePrice.open.toFloat(), minutePrice.close.toFloat()))
        }

        val set = CandleDataSet(yValues, viewModel.ticker)
        set.setDrawIcons(true)
        set.setDrawValues(false)
        set.axisDependency = AxisDependency.LEFT
        set.shadowColor = Color.DKGRAY
        set.decreasingColor = Color.RED
        set.decreasingPaintStyle = Paint.Style.FILL_AND_STROKE
        set.increasingColor = Color.GREEN
        set.increasingPaintStyle = Paint.Style.FILL_AND_STROKE
        set.neutralColor = Color.BLUE

        minuteChart.xAxis.position = XAxisPosition.BOTTOM
        minuteChart.xAxis.labelCount = 6
        minuteChart.xAxis.valueFormatter = IAxisValueFormatter { value, _ ->
            prices[value.toInt()].time.toHourAndMinute()
        }

        minuteChart.data = CandleData(set)
        minuteChart.legend.isEnabled = false
        minuteChart.description.text = ""
        minuteChart.invalidate()
    }

    private fun onCoinLoaded(coin: Coin) {
        coinLogoIv.loadImg(Conf.BASE_IMAGE_URL + coin.imageUrl)
        coinNameTv.text = coin.fullName
    }
}