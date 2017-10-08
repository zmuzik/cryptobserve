package zmuzik.cryptobserve.di


import dagger.Module
import dagger.android.ContributesAndroidInjector
import zmuzik.cryptobserve.screens.coinlist.CoinListActivity


@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    internal abstract fun bindCoinListActivity(): CoinListActivity
}