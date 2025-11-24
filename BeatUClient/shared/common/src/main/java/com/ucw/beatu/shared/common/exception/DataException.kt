package com.ucw.beatu.shared.common.exception

/**
 * 数据层异常基类
 */
sealed class DataException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    /**
     * 网络异常 - 无网络连接、超时等
     */
    class NetworkException(message: String, cause: Throwable? = null) : DataException(message, cause)

    /**
     * 服务器异常 - 服务器返回错误、5xx错误等
     */
    class ServerException(
        message: String,
        val code: Int? = null,
        cause: Throwable? = null
    ) : DataException(message, cause)

    /**
     * 数据库异常 - Room数据库操作失败
     */
    class DatabaseException(message: String, cause: Throwable? = null) : DataException(message, cause)

    /**
     * 未知异常 - 未预期的错误
     */
    class UnknownException(message: String, cause: Throwable? = null) : DataException(message, cause)

    /**
     * 认证异常 - Token过期、未授权等
     */
    class AuthException(message: String, val code: Int? = null, cause: Throwable? = null) :
        DataException(message, cause)

    /**
     * 资源未找到异常 - 404等
     */
    class NotFoundException(message: String, cause: Throwable? = null) : DataException(message, cause)
}

