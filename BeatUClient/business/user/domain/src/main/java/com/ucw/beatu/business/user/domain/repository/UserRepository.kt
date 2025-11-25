package com.ucw.beatu.business.user.domain.repository

import com.ucw.beatu.business.user.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * 用户仓储接口
 * 定义业务层需要的数据访问接口
 */
interface UserRepository {
    /**
     * 观察用户信息（Flow）
     */
    fun observeUserById(userId: String): Flow<User?>

    /**
     * 获取用户信息（一次性）
     */
    suspend fun getUserById(userId: String): User?

    /**
     * 保存用户信息
     */
    suspend fun saveUser(user: User)
}

