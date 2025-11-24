package com.ucw.beatu.shared.common.api

import com.squareup.moshi.JsonClass

/**
 * 统一API响应格式
 * 对应后端MySQL服务返回的响应结构
 */
@JsonClass(generateAdapter = true)
data class ApiResponse<T>(
    val code: Int,
    val message: String,
    val data: T?,
    val timestamp: Long? = null
) {
    /**
     * 判断请求是否成功
     * 通常code=200或0表示成功，具体根据后端定义调整
     */
    val isSuccess: Boolean get() = code == 200 || code == 0

    /**
     * 判断是否需要认证
     */
    val isUnauthorized: Boolean get() = code == 401 || code == 403

    /**
     * 判断资源是否存在
     */
    val isNotFound: Boolean get() = code == 404
}

/**
 * 分页响应包装
 */
@JsonClass(generateAdapter = true)
data class PageResponse<T>(
    val items: List<T>,
    val page: Int,
    val pageSize: Int,
    val total: Long,
    val totalPages: Int,
    val hasNext: Boolean = page < totalPages,
    val hasPrevious: Boolean = page > 1
)

