package com.ucw.beatu.business.videofeed.data.api.dto

import com.squareup.moshi.JsonClass

/**
 * 视频数据传输对象
 * 对应MySQL后端API返回的数据格式
 */
@JsonClass(generateAdapter = true)
data class VideoDto(
    val id: String,
    val playUrl: String,
    val coverUrl: String,
    val title: String,
    val tags: List<String>?,
    val durationMs: Long,
    val orientation: String,
    val authorId: String,
    val authorName: String,
    val authorAvatar: String? = null,
    val likeCount: Long = 0,
    val commentCount: Long = 0,
    val favoriteCount: Long = 0,
    val shareCount: Long = 0,
    val viewCount: Long = 0,
    val isLiked: Boolean = false,
    val isFavorited: Boolean = false,
    val isFollowedAuthor: Boolean = false,
    val createdAt: Long? = null,
    val updatedAt: Long? = null
)

