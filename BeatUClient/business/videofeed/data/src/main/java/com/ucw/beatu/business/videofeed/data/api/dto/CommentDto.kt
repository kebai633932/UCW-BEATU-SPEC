package com.ucw.beatu.business.videofeed.data.api.dto

import com.squareup.moshi.JsonClass

/**
 * 评论数据传输对象
 */
@JsonClass(generateAdapter = true)
data class CommentDto(
    val id: String,
    val videoId: String,
    val authorId: String,
    val authorName: String,
    val authorAvatar: String? = null,
    val content: String,
    val createdAt: Long,
    val isAiReply: Boolean = false,
    val likeCount: Long = 0
)

