package com.ucw.beatu.business.videofeed.domain.model

/**
 * 评论领域模型
 */
data class Comment(
    val id: String,
    val videoId: String,
    val authorId: String,
    val authorName: String,
    val authorAvatar: String? = null,
    val content: String,
    val createdAt: Long,
    val isAiReply: Boolean,
    val likeCount: Long = 0
)

