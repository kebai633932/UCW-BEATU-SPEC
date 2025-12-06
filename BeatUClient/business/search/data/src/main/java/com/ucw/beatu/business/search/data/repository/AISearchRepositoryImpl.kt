package com.ucw.beatu.business.search.data.repository

import com.ucw.beatu.business.search.data.api.AISearchApiService
import com.ucw.beatu.business.search.data.api.dto.AISearchStreamChunk
import com.ucw.beatu.business.search.domain.model.AISearchResult
import com.ucw.beatu.business.search.domain.repository.AISearchRepository
import com.ucw.beatu.shared.common.logger.AppLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import javax.inject.Inject

/**
 * AI 搜索 Repository 实现
 */
class AISearchRepositoryImpl @Inject constructor(
    private val apiService: AISearchApiService
) : AISearchRepository {
    
    companion object {
        private const val TAG = "AISearchRepository"
    }
    
    override fun searchStream(userQuery: String): Flow<AISearchResult> {
        return apiService.searchStream(userQuery)
            .map { chunk ->
                processChunk(chunk)
            }
    }
    
    /**
     * 处理数据块，转换为搜索结果
     */
    private fun processChunk(chunk: AISearchStreamChunk): AISearchResult {
        return when (chunk.chunkType) {
            "answer" -> {
                AISearchResult(aiAnswer = chunk.content)
            }
            "keywords" -> {
                try {
                    val keywords = parseJsonArray(chunk.content)
                    AISearchResult(keywords = keywords)
                } catch (e: Exception) {
                    AppLogger.e(TAG, "解析关键词失败", e)
                    AISearchResult()
                }
            }
            "videoIds" -> {
                try {
                    val videoIds = parseJsonArray(chunk.content)
                    AISearchResult(videoIds = videoIds)
                } catch (e: Exception) {
                    AppLogger.e(TAG, "解析视频 ID 失败", e)
                    AISearchResult()
                }
            }
            "localVideoIds" -> {
                try {
                    val localVideoIds = parseJsonArray(chunk.content)
                    AISearchResult(localVideoIds = localVideoIds)
                } catch (e: Exception) {
                    AppLogger.e(TAG, "解析本地视频 ID 失败", e)
                    AISearchResult()
                }
            }
            "error" -> {
                AISearchResult(error = chunk.content)
            }
            else -> {
                AISearchResult()
            }
        }
    }
    
    /**
     * 解析 JSON 数组字符串
     */
    private fun parseJsonArray(jsonString: String): List<String> {
        return try {
            val jsonArray = JSONArray(jsonString)
            (0 until jsonArray.length()).map { jsonArray.getString(it) }
        } catch (e: Exception) {
            AppLogger.e(TAG, "解析 JSON 数组失败: $jsonString", e)
            emptyList()
        }
    }
}

