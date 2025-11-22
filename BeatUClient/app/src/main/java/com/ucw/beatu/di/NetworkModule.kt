package com.ucw.beatu.di

import android.content.Context
import com.ucw.beatu.shared.network.config.NetworkConfig
import com.ucw.beatu.shared.network.config.OkHttpProvider
import com.ucw.beatu.shared.network.monitor.ConnectivityObserver
import com.ucw.beatu.shared.network.retrofit.RetrofitProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * 网络模块
 * 提供Retrofit、OkHttpClient、NetworkConfig等网络相关依赖注入
 * 配置MySQL后端服务连接
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * MySQL后端服务地址
     * TODO: 替换为实际的服务器地址
     */
    private const val BASE_URL = "http://your-mysql-backend-server.com/"

    @Provides
    @Singleton
    fun provideNetworkConfig(): NetworkConfig {
        return NetworkConfig(
            baseUrl = BASE_URL,
            connectTimeoutSeconds = 15,
            readTimeoutSeconds = 15,
            writeTimeoutSeconds = 15,
            enableLogging = true, // 开发环境开启日志
            defaultHeaders = mapOf(
                "Content-Type" to "application/json",
                "Accept" to "application/json"
                // TODO: 如果需要token认证，可以在这里添加 Authorization header
            )
        )
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        config: NetworkConfig,
        @ApplicationContext context: Context
    ): OkHttpClient {
        return OkHttpProvider.create(config, context.cacheDir)
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        config: NetworkConfig,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return RetrofitProvider.createRetrofit(config, okHttpClient)
    }

    @Provides
    @Singleton
    fun provideConnectivityObserver(
        @ApplicationContext context: Context
    ): ConnectivityObserver {
        return ConnectivityObserver(context)
    }
}

