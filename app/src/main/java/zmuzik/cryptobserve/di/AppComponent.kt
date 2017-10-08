package zmuzik.cryptobserve.di


import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import zmuzik.cryptobserve.App
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        AppModule::class,
        BuildersModule::class))

interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)
}