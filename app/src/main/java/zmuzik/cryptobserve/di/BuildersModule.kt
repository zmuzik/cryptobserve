package zmuzik.cryptobserve.di


import dagger.Module
import dagger.android.ContributesAndroidInjector
import zmuzik.cryptobserve.MainActivity


@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    internal abstract fun bindMainActivity(): MainActivity
}