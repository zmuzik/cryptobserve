package zmuzik.cryptobserve.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import zmuzik.cryptobserve.screens.coindetail.CoinDetailViewModel
import zmuzik.cryptobserve.screens.coinlist.CoinListViewModel
import zmuzik.cryptobserve.screens.coinpicker.CoinPickerViewModel

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CoinListViewModel::class)
    internal abstract fun bindCoinListViewModel(coinListViewModel: CoinListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CoinPickerViewModel::class)
    internal abstract fun bindCoinPickerViewModel(coinPickerViewModel: CoinPickerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CoinDetailViewModel::class)
    internal abstract fun bindCoinDetailViewModel(coinDetailViewModel: CoinDetailViewModel): ViewModel

}
