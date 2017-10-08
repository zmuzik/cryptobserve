package zmuzik.cryptobserve.di


import dagger.Module
import dagger.android.ContributesAndroidInjector
import zmuzik.cryptobserve.screens.coinlist.CoinListActivity
import zmuzik.cryptobserve.screens.coinpicker.CoinPickerActivity


@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    internal abstract fun bindCoinListActivity(): CoinListActivity

    @ContributesAndroidInjector
    internal abstract fun bindCoinPickerActivity(): CoinPickerActivity
}