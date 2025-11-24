package com.ucw.beatu.business.videofeed.data.api.dto

import com.squareup.moshi.JsonClass

/**
 * 发布评论请求DTO
 */
@JsonClass(generateAdapter = true)
data class CommentRequest(
    val content: String
)

