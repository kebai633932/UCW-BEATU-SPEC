package com.ucw.beatu.business.search.domain.model

/**
 * AI 搜索结果
 */
data class AISearchResult(
    val aiAnswer: String = "",
    val keywords: List<String> = emptyList(),
    val videoIds: List<String> = emptyList(),
    val localVideoIds: List<String> = emptyList(),
    val error: String? = null
)

