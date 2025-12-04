package com.ucw.beatu.shared.router

/**
 * Router 提供者接口
 * 用于在运行时获取 Router 实例，避免编译时依赖
 */
interface RouterProvider {
    /**
     * 获取 UserProfileRouter 实例
     */
    fun getUserProfileRouter(): UserProfileRouter?
    
    /**
     * 获取 VideoItemRouter 实例
     */
    fun getVideoItemRouter(): VideoItemRouter?
}

