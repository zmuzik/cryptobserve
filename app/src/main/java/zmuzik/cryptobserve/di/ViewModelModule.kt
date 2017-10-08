package zmuzik.cryptobserve.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import zmuzik.cryptobserve.screens.coinlist.CoinListViewModel

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CoinListViewModel::class)
    internal abstract fun bindCoinListViewModel(coinListViewModel: CoinListViewModel): ViewModel

}
