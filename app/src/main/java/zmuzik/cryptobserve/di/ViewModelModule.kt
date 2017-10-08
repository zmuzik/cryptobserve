package zmuzik.cryptobserve.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import zmuzik.cryptobserve.screens.coinslist.CoinsListViewModel

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CoinsListViewModel::class)
    internal abstract fun bindCoinsListViewModel(coinsListViewModel: CoinsListViewModel): ViewModel

}
