package com.ucw.beatu.shared.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ucw.beatu.shared.database.entity.VideoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {
    @Query("SELECT * FROM videos ORDER BY viewCount DESC LIMIT :limit")
    fun observeTopVideos(limit: Int): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE authorName = :authorName ORDER BY viewCount DESC LIMIT :limit")
    fun observeVideosByAuthorName(authorName: String, limit: Int): Flow<List<VideoEntity>>
    //todo   使用   user_video_relations  表
    @Query("SELECT * FROM videos WHERE isFavorited = 1 ORDER BY viewCount DESC LIMIT :limit")
    fun observeFavoritedVideos(limit: Int): Flow<List<VideoEntity>>
    //todo   使用   user_video_relations  表
    @Query("SELECT * FROM videos WHERE isLiked = 1 ORDER BY viewCount DESC LIMIT :limit")
    fun observeLikedVideos(limit: Int): Flow<List<VideoEntity>>

    @Query("""
        SELECT v.* FROM videos v
        INNER JOIN interaction_state i ON v.id = i.videoId
        WHERE i.lastSeekMs > 0
        ORDER BY i.lastSeekMs DESC
        LIMIT :limit
    """)
//todo     改用 user_video_relations   表
    fun observeHistoryVideos(limit: Int): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE id = :id LIMIT 1")
    suspend fun getVideoById(id: String): VideoEntity?

    @Query("SELECT * FROM videos WHERE id = :id LIMIT 1")
    fun observeVideoById(id: String): Flow<VideoEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<VideoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: VideoEntity)

    @Query("UPDATE videos SET coverUrl = :coverUrl WHERE id = :id")
    suspend fun updateCoverUrl(id: String, coverUrl: String)

    @Query("DELETE FROM videos")
    suspend fun clear()

    @Query("DELETE FROM videos WHERE id = :id")
    suspend fun deleteById(id: String)
}

