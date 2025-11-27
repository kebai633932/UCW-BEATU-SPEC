package com.ucw.beatu.business.videofeed.domain.usecase

import com.ucw.beatu.business.videofeed.domain.model.Video
import com.ucw.beatu.business.videofeed.domain.repository.VideoRepository
import com.ucw.beatu.shared.common.result.AppResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 获取视频流UseCase
 * 负责获取分页视频列表
 */
class GetFeedUseCase @Inject constructor(
    private val repository: VideoRepository
) {
    /**
     * 执行获取视频流
     * @param page 页码，从1开始
     * @param limit 每页数量
     * @param orientation 视频方向（可选）："portrait"、"landscape"，null表示不筛选
     * @return Flow<AppResult<List<Video>>> 响应式数据流
     */
    operator fun invoke(page: Int = 1, limit: Int = 20, orientation: String? = null): Flow<AppResult<List<Video>>> {
        return repository.getVideoFeed(page, limit, orientation)
    }
}

