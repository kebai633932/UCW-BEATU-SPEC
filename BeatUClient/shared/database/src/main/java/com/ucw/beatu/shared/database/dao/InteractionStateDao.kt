package com.ucw.beatu.shared.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ucw.beatu.shared.database.entity.InteractionStateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InteractionStateDao {
    @Query("SELECT * FROM interaction_state WHERE videoId = :videoId")
    fun observe(videoId: String): Flow<InteractionStateEntity?>

    @Query("SELECT videoId FROM interaction_state WHERE lastSeekMs > 0 ORDER BY lastSeekMs DESC LIMIT :limit")
    fun observePlayedVideoIds(limit: Int): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(state: InteractionStateEntity)
}

