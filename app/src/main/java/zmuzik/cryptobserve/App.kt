package zmuzik.cryptobserve

import android.app.Activity
import android.app.Application
import com.squareup.leakcanary.LeakCanary
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import zmuzik.cryptobserve.di.DaggerAppComponent
import javax.inject.Inject

class App : Application(), HasActivityInjector {

    @Inject lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) return
        if (BuildConfig.DEBUG) LeakCanary.install(this)

        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this)

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity>? = activityInjector
}