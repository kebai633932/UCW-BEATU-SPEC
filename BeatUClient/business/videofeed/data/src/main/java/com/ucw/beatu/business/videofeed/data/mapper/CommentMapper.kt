package com.ucw.beatu.business.videofeed.data.mapper

import com.ucw.beatu.business.videofeed.data.api.dto.CommentDto
import com.ucw.beatu.business.videofeed.domain.model.Comment
import com.ucw.beatu.shared.database.entity.CommentEntity

/**
 * 评论数据映射器
 */

/**
 * DTO -> Domain Model
 */
fun CommentDto.toDomain(): Comment {
    return Comment(
        id = id,
        videoId = videoId,
        authorId = authorId,
        authorName = authorName,
        authorAvatar = authorAvatar,
        content = content,
        createdAt = createdAt,
        isAiReply = isAiReply,
        likeCount = likeCount
    )
}

/**
 * Entity -> Domain Model
 */
fun CommentEntity.toDomain(): Comment {
    return Comment(
        id = id,
        videoId = videoId,
        authorId = authorId,
        authorName = authorName,
        authorAvatar = null,
        content = content,
        createdAt = createdAt,
        isAiReply = isAiReply,
        likeCount = 0
    )
}

/**
 * Domain Model -> Entity
 */
fun Comment.toEntity(): CommentEntity {
    return CommentEntity(
        id = id,
        videoId = videoId,
        authorId = authorId,
        authorName = authorName,
        content = content,
        createdAt = createdAt,
        isAiReply = isAiReply
    )
}

