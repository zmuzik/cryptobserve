package zmuzik.cryptobserve.screens.coinslist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import zmuzik.cryptobserve.R
import zmuzik.cryptobserve.di.ViewModelFactory
import zmuzik.cryptobserve.repo.entities.Coin
import javax.inject.Inject

class CoinsListActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: CoinsListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CoinsListViewModel::class.java)
        viewModel.getAllCoins().observe(this, Observer { it?.let { onCoinsLoaded(it) } })
    }

    private fun onCoinsLoaded(coins: List<Coin>) {
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
}
