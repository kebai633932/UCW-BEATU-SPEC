package com.ucw.beatu.business.search.data.api.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * AI 搜索请求
 */
@JsonClass(generateAdapter = true)
data class AISearchRequest(
    @Json(name = "userQuery")
    val userQuery: String
)

