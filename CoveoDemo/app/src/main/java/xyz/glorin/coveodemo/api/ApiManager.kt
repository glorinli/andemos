package xyz.glorin.coveodemo.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.math.log

object ApiManager {
    const val BASE_URL_COVEO = "https://platform.cloud.coveo.com/"

    private val okHttpClient: OkHttpClient by lazy {
        val logger = HttpLoggingInterceptor {
            Log.d("ApiManager", it)
        }
        logger.level = HttpLoggingInterceptor.Level.BODY

        OkHttpClient.Builder()
            .addInterceptor(logger)
            .addInterceptor(CoveoInterceptor())
            .build()
    }

    val coveoApi: CoveoApi by lazy {
        create(BASE_URL_COVEO, CoveoApi::class.java)
    }

    val networkExecutor: Executor by lazy {
        Executors.newFixedThreadPool(5)
    }

    fun <T> create(baseUrl: String, clazz: Class<T>): T {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(clazz)
    }
}