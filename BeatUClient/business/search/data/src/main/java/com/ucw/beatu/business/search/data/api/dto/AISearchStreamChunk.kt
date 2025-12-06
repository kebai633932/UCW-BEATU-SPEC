package com.ucw.beatu.business.search.data.api.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * AI 搜索流式数据块
 */
@JsonClass(generateAdapter = true)
data class AISearchStreamChunk(
    @Json(name = "chunkType")
    val chunkType: String, // answer, keywords, videoIds, localVideoIds, error
    
    @Json(name = "content")
    val content: String,
    
    @Json(name = "isFinal")
    val isFinal: Boolean
)

