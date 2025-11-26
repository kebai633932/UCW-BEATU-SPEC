package com.ucw.beatu.business.user.data.local

import com.ucw.beatu.business.user.data.mapper.toDomain
import com.ucw.beatu.business.user.data.mapper.toEntity
import com.ucw.beatu.business.user.domain.model.User
import com.ucw.beatu.shared.database.BeatUDatabase
import com.ucw.beatu.shared.database.dao.UserDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 用户本地数据源接口
 */
interface UserLocalDataSource {
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

    /**
     * 批量保存用户信息
     */
    suspend fun saveUsers(users: List<User>)
}

/**
 * 用户本地数据源实现
 * 负责从Room数据库读写用户数据
 */
class UserLocalDataSourceImpl @Inject constructor(
    private val database: BeatUDatabase
) : UserLocalDataSource {

    private val userDao: UserDao = database.userDao()

    override fun observeUserById(userId: String): Flow<User?> {
        return userDao.observeUserById(userId).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun getUserById(userId: String): User? {
        return userDao.getUserById(userId)?.toDomain()
    }

    override suspend fun saveUser(user: User) {
        userDao.insertOrUpdate(user.toEntity())
    }

    override suspend fun saveUsers(users: List<User>) {
        userDao.insertOrUpdateAll(users.map { it.toEntity() })
    }
}

