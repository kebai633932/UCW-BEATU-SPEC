package com.ucw.beatu.shared.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 用户-视频关系实体（连接表）
 * 记录用户发布的视频
 */
@Entity(
    tableName = "user_video_relations",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = VideoEntity::class,
            parentColumns = ["id"],
            childColumns = ["videoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["userId"]),
        Index(value = ["videoId"]),
        Index(value = ["userId", "videoId"], unique = true)
    ]
)
data class UserVideoRelationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String, // 用户ID
    val videoId: String // 视频ID
)

