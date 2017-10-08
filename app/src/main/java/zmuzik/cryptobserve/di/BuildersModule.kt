package zmuzik.cryptobserve.di


import dagger.Module
import dagger.android.ContributesAndroidInjector
import zmuzik.cryptobserve.screens.coinslist.CoinsListActivity


@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    internal abstract fun bindCoinsListActivity(): CoinsListActivity
}