package com.ucw.beatu.shared.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 用户实体
 * 存储用户基本信息
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val avatarUrl: String?, // 头像（图片路径）
    val name: String, // 名称
    val bio: String?, // 名言/简介
    val likesCount: Long = 0, // 获赞
    val followingCount: Long = 0, // 关注
    val followersCount: Long = 0 // 粉丝
)

