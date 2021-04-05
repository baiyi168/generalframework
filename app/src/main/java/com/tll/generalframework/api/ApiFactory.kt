package com.tll.generalframework.api

import com.tll.generalframework.BaseApplication
import com.tll.generalframework.api.interceptor.HttpLoggingInterceptor
import com.tll.generalframework.api.interceptor.SignInterceptor
import com.tll.generalframework.config.BaseAppConfig
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class ApiFactory {
    companion object {
        val sInstance: ApiFactory by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ApiFactory()
        }
    }

    private var mRetrofit: Retrofit
    private var client: OkHttpClient

    private constructor() {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val size = 1024 * 1024 * 10
        val cacheFile = File(BaseApplication.instance.cacheDir, "okHttp")
        val cache = Cache(cacheFile, size.toLong())
        client = OkHttpClient.Builder()
            .connectTimeout(12, TimeUnit.SECONDS)
            .writeTimeout(12, TimeUnit.SECONDS)
            .addInterceptor(SignInterceptor())
            .addInterceptor(loggingInterceptor)
            .cache(cache)
            .build()

        mRetrofit = Retrofit.Builder()
            .baseUrl(BaseAppConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    fun getApiService(): ApiService {
        return mRetrofit.create(ApiService::class.java)
    }
}