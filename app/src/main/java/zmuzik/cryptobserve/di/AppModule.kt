package zmuzik.cryptobserve.di


import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import zmuzik.cryptobserve.App
import javax.inject.Singleton


@Module
class AppModule {

    @Provides
    fun provideContext(app: App): Context = app.applicationContext

}
