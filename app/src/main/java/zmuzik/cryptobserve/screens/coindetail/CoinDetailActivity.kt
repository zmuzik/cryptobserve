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
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
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
        setupChart()
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
        setupDataSet(set)

        minuteChart.xAxis.valueFormatter = IAxisValueFormatter { value, _ ->
            prices[value.toInt()].time.toHourAndMinute()
        }

        minuteChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(entry: Entry?, h: Highlight?) {
                entry?.let {
                    val price = prices[it.x.toInt()]
                    timeTv.text = price.time.toDateTime()
                    timeFrameTv.text = "5 min"
                    openTv.text = price.open.format()
                    closeTv.text = price.close.format()
                    lowTv.text = price.low.format()
                    highTv.text = price.high.format()
                }
            }

            override fun onNothingSelected() {}
        })

        minuteChart.data = CandleData(set)
        minuteChart.invalidate()
    }

    private fun onCoinLoaded(coin: Coin) {
        coinLogoIv.loadImg(Conf.BASE_IMAGE_URL + coin.imageUrl)
        coinNameTv.text = coin.fullName
    }

    private fun setupChart() {
        with(minuteChart) {
            isDoubleTapToZoomEnabled = false
            legend.isEnabled = false
            description.text = ""
            axisLeft.gridLineWidth = 1f
            xAxis.position = XAxisPosition.BOTTOM
            xAxis.labelCount = 6
            xAxis.gridLineWidth = 1f
        }
    }

    private fun setupDataSet(set: CandleDataSet) {
        with(set) {
            setDrawIcons(true)
            setDrawValues(false)
            axisDependency = AxisDependency.LEFT
            shadowColor = Color.DKGRAY
            decreasingColor = Color.RED
            decreasingPaintStyle = Paint.Style.FILL_AND_STROKE
            increasingColor = Color.GREEN
            increasingPaintStyle = Paint.Style.FILL_AND_STROKE
            neutralColor = Color.BLUE
        }
    }
}