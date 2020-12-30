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
    private const val BASE_URL_COVEO = "https://platform.cloud.coveo.com/"

    private val okHttpClient: OkHttpClient by lazy {
        val logger = HttpLoggingInterceptor {
            Log.d("ApiManager", it)
        }
        logger.level = HttpLoggingInterceptor.Level.BASIC

        val tokenInterceptor = Interceptor { chain ->
            if (chain.request().url().toString().contains(BASE_URL_COVEO)) {
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer xx854468e8-1a94-4988-bc3e-dddd23c56930")
                    .build()
                chain.proceed(request)
            } else {
                chain.proceed(chain.request())
            }
        }

        OkHttpClient.Builder()
            .addInterceptor(logger)
            .addInterceptor(tokenInterceptor)
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