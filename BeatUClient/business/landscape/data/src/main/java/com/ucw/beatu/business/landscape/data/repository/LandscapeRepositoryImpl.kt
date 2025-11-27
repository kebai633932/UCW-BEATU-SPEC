package com.ucw.beatu.business.landscape.data.repository

import com.ucw.beatu.business.landscape.data.mapper.toLandscapeVideoItem
import com.ucw.beatu.business.landscape.domain.model.VideoItem
import com.ucw.beatu.business.landscape.domain.repository.LandscapeRepository
import com.ucw.beatu.business.videofeed.domain.repository.VideoRepository
import com.ucw.beatu.shared.common.result.AppResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 横屏视频仓储实现
 * 通过VideoFeed的VideoRepository获取横屏视频数据
 */
@Singleton
class LandscapeRepositoryImpl @Inject constructor(
    private val videoRepository: VideoRepository
) : LandscapeRepository {

    override fun getLandscapeVideos(page: Int, limit: Int): Flow<AppResult<List<VideoItem>>> {
        // 通过VideoRepository获取横屏视频（orientation="landscape"）
        return videoRepository.getVideoFeed(page, limit, "landscape")
            .map { result ->
                when (result) {
                    is AppResult.Success -> {
                        AppResult.Success(result.data.map { it.toLandscapeVideoItem() })
                    }
                    is AppResult.Error -> result
                    is AppResult.Loading -> result
                }
            }
    }

    override fun loadMoreLandscapeVideos(page: Int, limit: Int): Flow<AppResult<List<VideoItem>>> {
        // 通过VideoRepository获取更多横屏视频
        return videoRepository.getVideoFeed(page, limit, "landscape")
            .map { result ->
                when (result) {
                    is AppResult.Success -> {
                        AppResult.Success(result.data.map { it.toLandscapeVideoItem() })
                    }
                    is AppResult.Error -> result
                    is AppResult.Loading -> result
                }
            }
    }
}


