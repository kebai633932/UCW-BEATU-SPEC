package com.ucw.beatu.business.videofeed.domain.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * VideoFeed Domain层依赖注入模块
 * 所有UseCase通过构造函数注入，无需额外配置
 * Hilt会自动处理UseCase的依赖注入
 */
@Module
@InstallIn(SingletonComponent::class)
object VideoFeedDomainModule {
    // UseCase通过构造函数注入，Hilt会自动处理
    // 这里不需要额外的Provides方法
}

