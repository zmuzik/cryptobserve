package zmuzik.cryptobserve.screens.coindetail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import com.github.mikephil.charting.components.XAxis
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
import timber.log.Timber
import zmuzik.cryptobserve.*
import zmuzik.cryptobserve.di.ViewModelFactory
import zmuzik.cryptobserve.repo.entities.Coin
import zmuzik.cryptobserve.repo.entities.FavCoinListItem
import zmuzik.cryptobserve.repo.entities.HistPrice
import zmuzik.cryptobserve.repo.entities.Timeframe
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

        setTimeFrame(Timeframe.HOUR)
        setupChart()
        setupTabs()
    }

    @Synchronized
    private fun onHistPricesLoaded(prices: List<HistPrice>) {
        if (prices.isEmpty()) return

        bindPrice(prices.last())

        chart.clear()
        val yValues = ArrayList<CandleEntry>()
        prices.forEachIndexed { index, minutePrice ->
            yValues.add(CandleEntry(index.toFloat(),
                    minutePrice.high.toFloat(),
                    minutePrice.low.toFloat(),
                    minutePrice.open.toFloat(),
                    minutePrice.close.toFloat(),
                    minutePrice))
        }

        val set = CandleDataSet(yValues, viewModel.ticker)
        setupDataSet(set)

        chart.xAxis.valueFormatter = IAxisValueFormatter { value, _ ->
            try {
                val price = set.getEntryForIndex(value.toInt()).data as HistPrice
                when (price.timeFrame) {
                    Timeframe.HOUR.name -> price.time.toHourAndMinute()
                    Timeframe.DAY.name -> price.time.toDateTimeShort()
                    Timeframe.WEEK.name -> price.time.toDateTimeShort()
                    else -> price.time.toMonthDay()
                }
            } catch (e: IndexOutOfBoundsException) {
                Timber.e(e.message)
                ""
            }
        }

        chart.data = CandleData(set)
        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(entry: Entry?, h: Highlight?) {
                entry?.let { bindPrice(prices[it.x.toInt()]) }
            }

            override fun onNothingSelected() {}
        })
        chart.invalidate()
    }

    private fun onCoinLoaded(coin: Coin) {
        coinLogoIv.loadImg(Conf.BASE_IMAGE_URL + coin.imageUrl)
        coinNameTv.text = coin.fullName
    }

    private fun bindPrice(price: HistPrice) {
        with(price) {
            aggregationTv.text = when (price.timeFrame) {
                Timeframe.HOUR.name -> getString(R.string.aggregation_1_min)
                Timeframe.DAY.name -> getString(R.string.aggregation_5_min)
                Timeframe.WEEK.name -> getString(R.string.aggregation_1_hr)
                else -> getString(R.string.aggregation_1_day)
            }
            timeTv.text = when (price.timeFrame) {
                Timeframe.HOUR.name -> time.toHourAndMinute()
                Timeframe.DAY.name -> time.toDateTime()
                Timeframe.WEEK.name -> time.toDateTime()
                else -> time.toYearMonthDay()
            }
            openTv.text = open.format()
            closeTv.text = close.format()
            lowTv.text = low.format()
            highTv.text = high.format()
        }
    }

    fun setTimeFrame(timeframe: Timeframe) {
        viewModel.removePriceObservers(this)
        chart.clear()
        viewModel.timeframe = timeframe
        viewModel.getHistPrices().observe(this, Observer { it?.let { onHistPricesLoaded(it) } })
        viewModel.requestUpdate()
    }

    private fun setupChart() {
        with(chart) {
            isDoubleTapToZoomEnabled = false
            legend.isEnabled = false
            description.text = ""
            axisLeft.gridLineWidth = 1f
            xAxis.position = XAxis.XAxisPosition.BOTTOM
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

    private fun setupTabs() {
        buttonPanel.addTab(buttonPanel.newTab().setText(getString(R.string.one_year)))
        buttonPanel.addTab(buttonPanel.newTab().setText(getString(R.string.one_month)))
        buttonPanel.addTab(buttonPanel.newTab().setText(getString(R.string.one_week)))
        buttonPanel.addTab(buttonPanel.newTab().setText(getString(R.string.one_day)))
        buttonPanel.addTab(buttonPanel.newTab().setText(getString(R.string.one_hour)))
        buttonPanel.getTabAt(4)?.select()
        buttonPanel.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (buttonPanel.selectedTabPosition) {
                    0 -> setTimeFrame(Timeframe.YEAR)
                    1 -> setTimeFrame(Timeframe.MONTH)
                    2 -> setTimeFrame(Timeframe.WEEK)
                    3 -> setTimeFrame(Timeframe.DAY)
                    4 -> setTimeFrame(Timeframe.HOUR)
                }
            }
        })
    }
}