package com.ucw.beatu.business.videofeed.domain.usecase

import com.ucw.beatu.business.settings.domain.repository.SettingsRepository
import com.ucw.beatu.business.settings.domain.model.PlaybackQualityPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 获取播放设置UseCase
 * 从Settings模块读取播放配置（清晰度、倍速等）
 */
class GetPlaybackSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    /**
     * 获取默认播放倍速
     */
    fun getDefaultSpeed(): Flow<Float> {
        return settingsRepository.observeSettings()
            .map { it.defaultSpeed }
    }

    /**
     * 获取默认播放清晰度
     */
    fun getDefaultQuality(): Flow<PlaybackQualityPreference> {
        return settingsRepository.observeSettings()
            .map { it.defaultQuality }
    }

    /**
     * 获取自动播放设置
     */
    fun getAutoPlayEnabled(): Flow<Boolean> {
        return settingsRepository.observeSettings()
            .map { it.autoPlayEnabled }
    }
}

