package com.ucw.beatu.business.videofeed.data.local

import com.ucw.beatu.business.videofeed.data.mapper.toDomain
import com.ucw.beatu.business.videofeed.data.mapper.toEntity
import com.ucw.beatu.business.videofeed.domain.model.Comment
import com.ucw.beatu.business.videofeed.domain.model.Video
import com.ucw.beatu.shared.database.BeatUDatabase
import com.ucw.beatu.shared.database.dao.CommentDao
import com.ucw.beatu.shared.database.dao.VideoDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 本地数据源接口
 */
interface VideoLocalDataSource {
    fun observeVideos(limit: Int = 50): Flow<List<Video>>
    suspend fun getVideoById(id: String): Video?
    fun observeVideoById(id: String): Flow<Video?>
    suspend fun saveVideos(videos: List<Video>)
    suspend fun saveVideo(video: Video)
    suspend fun clearVideos()

    fun observeComments(videoId: String): Flow<List<Comment>>
    suspend fun getCommentById(commentId: String): Comment?
    suspend fun saveComments(comments: List<Comment>)
    suspend fun saveComment(comment: Comment)
    suspend fun clearComments(videoId: String)
}

/**
 * 本地数据源实现
 * 负责从Room数据库读写数据
 */
class VideoLocalDataSourceImpl @Inject constructor(
    private val database: BeatUDatabase
) : VideoLocalDataSource {

    private val videoDao: VideoDao = database.videoDao()
    private val commentDao: CommentDao = database.commentDao()

    override fun observeVideos(limit: Int): Flow<List<Video>> {
        return videoDao.observeTopVideos(limit).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getVideoById(id: String): Video? {
        return videoDao.getVideoById(id)?.toDomain()
    }

    override fun observeVideoById(id: String): Flow<Video?> {
        return videoDao.observeVideoById(id).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun saveVideos(videos: List<Video>) {
        videoDao.insertAll(videos.map { it.toEntity() })
    }

    override suspend fun saveVideo(video: Video) {
        videoDao.insert(video.toEntity())
    }

    override suspend fun clearVideos() {
        videoDao.clear()
    }

    override fun observeComments(videoId: String): Flow<List<Comment>> {
        return commentDao.observeComments(videoId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getCommentById(commentId: String): Comment? {
        return commentDao.getCommentById(commentId)?.toDomain()
    }

    override suspend fun saveComments(comments: List<Comment>) {
        commentDao.insertAll(comments.map { it.toEntity() })
    }

    override suspend fun saveComment(comment: Comment) {
        commentDao.insert(comment.toEntity())
    }

    override suspend fun clearComments(videoId: String) {
        commentDao.clear(videoId)
    }
}

