package com.ucw.beatu.business.user.data.repository

import com.ucw.beatu.business.user.data.local.UserLocalDataSource
import com.ucw.beatu.business.user.domain.model.User
import com.ucw.beatu.business.user.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 用户仓储实现
 * 协调本地数据源
 */
class UserRepositoryImpl @Inject constructor(
    private val localDataSource: UserLocalDataSource
) : UserRepository {

    override fun observeUserById(userId: String): Flow<User?> {
        return localDataSource.observeUserById(userId)
    }

    override suspend fun getUserById(userId: String): User? {
        return localDataSource.getUserById(userId)
    }

    override suspend fun saveUser(user: User) {
        localDataSource.saveUser(user)
    }
}

