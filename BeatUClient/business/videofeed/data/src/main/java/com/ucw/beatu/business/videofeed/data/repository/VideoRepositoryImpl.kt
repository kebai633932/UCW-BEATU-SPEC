package com.ucw.beatu.business.videofeed.data.repository

import com.ucw.beatu.business.videofeed.data.local.VideoLocalDataSource
import com.ucw.beatu.business.videofeed.data.remote.VideoRemoteDataSource
import com.ucw.beatu.business.videofeed.domain.model.Comment
import com.ucw.beatu.business.videofeed.domain.model.Video
import com.ucw.beatu.business.videofeed.domain.repository.VideoRepository
import com.ucw.beatu.shared.common.result.AppResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/**
 * 视频仓储实现
 * 协调本地和远程数据源，实现缓存策略
 */
class VideoRepositoryImpl @Inject constructor(
    private val remoteDataSource: VideoRemoteDataSource,
    private val localDataSource: VideoLocalDataSource
) : VideoRepository {

    override fun getVideoFeed(page: Int, limit: Int): Flow<AppResult<List<Video>>> = flow {
        // 如果是第一页，先尝试从本地获取并发送
        if (page == 1) {
            val localVideos = localDataSource.observeVideos(limit).firstOrNull() ?: emptyList()
            if (localVideos.isNotEmpty()) {
                emit(AppResult.Success(localVideos))
            }
        }

        // 从远程获取最新数据
        val remoteResult = remoteDataSource.getVideoFeed(page, limit)
        when (remoteResult) {
            is AppResult.Success -> {
                // 保存到本地缓存（仅第一页）
                if (page == 1) {
                    localDataSource.saveVideos(remoteResult.data)
                }
                emit(remoteResult)
            }
            is AppResult.Error -> {
                // 如果远程失败且是第一页，且已发送本地数据，不再发送错误
                // 如果是后续页或没有本地数据，发出错误
                if (page != 1 || localDataSource.observeVideos(limit).firstOrNull()?.isEmpty() != false) {
                    emit(remoteResult)
                }
            }
            is AppResult.Loading -> emit(remoteResult)
        }
    }.onStart { emit(AppResult.Loading) }
        .catch { emit(AppResult.Error(it)) }

    override suspend fun getVideoDetail(videoId: String): AppResult<Video> {
        // 先检查本地缓存
        localDataSource.getVideoById(videoId)?.let {
            // 异步获取最新数据并更新缓存
            val remoteResult = remoteDataSource.getVideoDetail(videoId)
            if (remoteResult is AppResult.Success) {
                localDataSource.saveVideo(remoteResult.data)
                return remoteResult
            }
            // 如果远程获取失败，返回本地缓存
            return AppResult.Success(it)
        }

        // 本地没有缓存，从远程获取
        return when (val result = remoteDataSource.getVideoDetail(videoId)) {
            is AppResult.Success -> {
                localDataSource.saveVideo(result.data)
                result
            }
            else -> result
        }
    }

    override fun getComments(videoId: String, page: Int, limit: Int): Flow<AppResult<List<Comment>>> = flow {
        // 如果是第一页，先尝试从本地获取并发送
        if (page == 1) {
            val localComments = localDataSource.observeComments(videoId).firstOrNull() ?: emptyList()
            if (localComments.isNotEmpty()) {
                emit(AppResult.Success(localComments))
            }
        }

        // 从远程获取
        val remoteResult = remoteDataSource.getComments(videoId, page, limit)
        when (remoteResult) {
            is AppResult.Success -> {
                if (page == 1) {
                    localDataSource.saveComments(remoteResult.data)
                }
                emit(remoteResult)
            }
            is AppResult.Error -> {
                // 如果远程失败且是第一页，且已发送本地数据，不再发送错误
                if (page != 1 || localDataSource.observeComments(videoId).firstOrNull()?.isEmpty() != false) {
                    emit(remoteResult)
                }
            }
            is AppResult.Loading -> emit(remoteResult)
        }
    }.onStart { emit(AppResult.Loading) }
        .catch { emit(AppResult.Error(it)) }

    override suspend fun likeVideo(videoId: String): AppResult<Unit> {
        return remoteDataSource.likeVideo(videoId)
    }

    override suspend fun unlikeVideo(videoId: String): AppResult<Unit> {
        return remoteDataSource.unlikeVideo(videoId)
    }

    override suspend fun favoriteVideo(videoId: String): AppResult<Unit> {
        return remoteDataSource.favoriteVideo(videoId)
    }

    override suspend fun unfavoriteVideo(videoId: String): AppResult<Unit> {
        return remoteDataSource.unfavoriteVideo(videoId)
    }

    override suspend fun postComment(videoId: String, content: String): AppResult<Comment> {
        return when (val result = remoteDataSource.postComment(videoId, content)) {
            is AppResult.Success -> {
                // 保存新评论到本地
                localDataSource.saveComment(result.data)
                result
            }
            else -> result
        }
    }
}

