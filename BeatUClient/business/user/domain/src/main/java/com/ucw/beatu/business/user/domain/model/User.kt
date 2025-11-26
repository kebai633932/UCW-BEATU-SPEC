package com.ucw.beatu.business.user.domain.model

/**
 * 用户领域模型
 */
data class User(
    val id: String,
    val avatarUrl: String?, // 头像（图片路径）
    val name: String, // 名称
    val bio: String?, // 名言/简介
    val likesCount: Long = 0, // 获赞
    val followingCount: Long = 0, // 关注
    val followersCount: Long = 0 // 粉丝
)

