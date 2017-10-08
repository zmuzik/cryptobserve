package zmuzik.cryptobserve.di


import android.content.Context
import dagger.Module
import dagger.Provides
import zmuzik.cryptobserve.App
import zmuzik.cryptobserve.repo.DefaultRepo
import zmuzik.cryptobserve.repo.Repo
import javax.inject.Singleton


@Module(includes = arrayOf(ViewModelModule::class))
class AppModule {

    @Provides
    fun provideContext(app: App): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideRepo(): Repo = DefaultRepo()

}
