package zmuzik.cryptobserve.di


import android.arch.persistence.room.Room
import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import zmuzik.cryptobserve.App
import zmuzik.cryptobserve.BuildConfig
import zmuzik.cryptobserve.Conf
import zmuzik.cryptobserve.repo.Api
import zmuzik.cryptobserve.repo.Db
import zmuzik.cryptobserve.repo.DefaultRepo
import zmuzik.cryptobserve.repo.Repo
import javax.inject.Singleton


@Module(includes = arrayOf(ViewModelModule::class))
class AppModule {

    @Provides
    fun provideContext(app: App): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideRepo(db: Db, api: Api): Repo = DefaultRepo(db, api)

    @Provides
    @Singleton
    fun provideDb(app: App): Db = Room.databaseBuilder(app, Db::class.java, "db").build()

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            client.addInterceptor(loggingInterceptor)
        }
        return client.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(Conf.API_ROOT)
                .client(client)
                .build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): Api = retrofit.create(Api::class.java)
}
