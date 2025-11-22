package com.ucw.beatu.business.videofeed.domain.model

/**
 * 视频领域模型
 * 业务层的核心数据模型，不依赖具体实现
 */
data class Video(
    val id: String,
    val playUrl: String,
    val coverUrl: String,
    val title: String,
    val tags: List<String>,
    val durationMs: Long,
    val orientation: String,
    val authorId: String,
    val authorName: String,
    val authorAvatar: String? = null,
    val likeCount: Long,
    val commentCount: Long,
    val favoriteCount: Long,
    val shareCount: Long,
    val viewCount: Long,
    val isLiked: Boolean,
    val isFavorited: Boolean,
    val isFollowedAuthor: Boolean,
    val createdAt: Long? = null,
    val updatedAt: Long? = null
)

