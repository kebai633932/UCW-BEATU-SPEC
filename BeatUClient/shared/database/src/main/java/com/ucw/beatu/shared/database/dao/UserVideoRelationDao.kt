package com.ucw.beatu.shared.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ucw.beatu.shared.database.entity.UserVideoRelationEntity
import kotlinx.coroutines.flow.Flow

/**
 * 用户-视频关系数据访问对象
 */
@Dao
interface UserVideoRelationDao {
    /**
     * 根据用户ID查询该用户的所有视频ID（Flow）
     */
    @Query("SELECT videoId FROM user_video_relations WHERE userId = :userId")
    fun observeVideoIdsByUserId(userId: String): Flow<List<String>>

    /**
     * 根据用户ID查询该用户的所有视频ID（一次性）
     */
    @Query("SELECT videoId FROM user_video_relations WHERE userId = :userId")
    suspend fun getVideoIdsByUserId(userId: String): List<String>

    /**
     * 根据视频ID查询作者用户ID
     */
    @Query("SELECT userId FROM user_video_relations WHERE videoId = :videoId LIMIT 1")
    suspend fun getUserIdByVideoId(videoId: String): String?

    /**
     * 插入用户-视频关系
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(relation: UserVideoRelationEntity)

    /**
     * 批量插入用户-视频关系
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(relations: List<UserVideoRelationEntity>)

    /**
     * 删除用户的所有视频关系
     */
    @Query("DELETE FROM user_video_relations WHERE userId = :userId")
    suspend fun deleteByUserId(userId: String)

    /**
     * 删除视频的关系
     */
    @Query("DELETE FROM user_video_relations WHERE videoId = :videoId")
    suspend fun deleteByVideoId(videoId: String)

    /**
     * 删除特定关系
     */
    @Query("DELETE FROM user_video_relations WHERE userId = :userId AND videoId = :videoId")
    suspend fun delete(userId: String, videoId: String)

    /**
     * 清空所有关系
     */
    @Query("DELETE FROM user_video_relations")
    suspend fun clear()
}

