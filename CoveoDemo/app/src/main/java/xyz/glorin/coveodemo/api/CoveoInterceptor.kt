package xyz.glorin.coveodemo.api

import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

class CoveoInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (chain.request().url().toString().contains(ApiManager.BASE_URL_COVEO)) {
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer xx854468e8-1a94-4988-bc3e-dddd23c56930")
                .url(
                    chain.request()
                        .url()
                        .newBuilder()
                        .addQueryParameter("timezone", getTimeZone())
                        .addQueryParameter("locale", getLocale())
                        .build()
                )
                .build()
            chain.proceed(request)
        } else {
            chain.proceed(chain.request())
        }
    }

    private fun getLocale(): String {
        return Locale.getDefault().toLanguageTag()
    }

    private fun getTimeZone(): String {
        return "Asia/Shanghai"
    }
}