package com.example.pokedex.core.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkService {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(createClient())
            .build()
    }

    private fun createClient(): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            .connectTimeout(0, TimeUnit.SECONDS)
            .writeTimeout(0, TimeUnit.SECONDS)
            .readTimeout(0, TimeUnit.SECONDS)
        // if (BuildConfig.DEBUG) {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        okHttpClientBuilder.addInterceptor { chain ->
            var request = chain.request()
            bodyToString(request.body)
            if (chain.request().header("No-Authentication") == null) {
                runBlocking {
                    val token = getToken()
                    request = request.newBuilder().addHeader(
                        "Authorization",
                        "Bearer $token"
                    ).build()
                }
            }
            chain.proceed(request)
        }.apply {
            this.addInterceptor(loggingInterceptor)
        }
        //  }
        return okHttpClientBuilder.build()
    }

    private suspend fun getToken(): String? {
        var token: String? = ""
        try {
            val mUser = FirebaseAuth.getInstance().currentUser
            val tokenResult = mUser!!.getIdToken(true).await()
            token = tokenResult.token
            if (BuildConfig.DEBUG) {
                Log.i("TOKEN", "$token")
                Global.token= token.orEmpty()
            }
        } catch (e: Exception) {
            Log.e("FirebaseGetToken", "ErrorGetToken", e)
        }
        return token
    }

    private fun bodyToString(request: RequestBody?) {
        try {
            val copy: RequestBody? = request

            val buffer = Buffer()
            copy?.writeTo(buffer)
            Log.e("tostring", buffer.readUtf8())
        } catch (e: IOException) {
            Log.e("bodyToStrm", "did not work")
        }
    }

    @Provides
    @Singleton
    fun provideHomeRepository(dataSource: HomeRepository.Network): HomeRepository = dataSource

    @Provides
    @Singleton
    fun provideRegisterRepository(dataSource: RegisterRepository.Network): RegisterRepository =
        dataSource

    @Provides
    @Singleton
    fun providePlanRepository(dataSource: PlanRepository.Network): PlanRepository = dataSource

    @Provides
    @Singleton
    fun provideAwardRepository(dataSource: AwardRepository.Network): AwardRepository = dataSource

    @Provides
    @Singleton
    fun provideTransactionRepository(dataSource: TransactionRepository.Network): TransactionRepository = dataSource

    @Provides
    @Singleton
    fun provideStampRepository(dataSource: StampRepository.Network): StampRepository = dataSource

    @Provides
    @Singleton
    fun provideSearchRepository(dataSource: SearchRepository.Network): SearchRepository = dataSource


    @Provides
    @Singleton
    fun provideFavoriteRepository(dataSource: FavoriteRepository.Network): FavoriteRepository = dataSource
    @Provides
    @Singleton
    fun provideNotificationRepository(dataSource: NotificationRepository.Network): NotificationRepository = dataSource

}
