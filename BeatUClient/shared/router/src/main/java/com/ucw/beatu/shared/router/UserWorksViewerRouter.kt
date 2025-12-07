package com.ucw.beatu.shared.router

import com.ucw.beatu.shared.common.model.VideoItem

/**
 * 用户作品播放器路由接口
 * 用于解耦 videofeed 和 user 模块之间的循环依赖
 * 
 * user 模块实现此接口，videofeed 模块使用此接口
 */
interface UserWorksViewerRouter {
    /**
     * 切换到指定索引的视频
     * @param index 视频索引
     * @return 是否成功切换（如果当前不在 UserWorksViewerFragment 中，返回 false）
     */
    fun switchToVideo(index: Int): Boolean
    
    /**
     * 获取当前用户 ID
     * @return 当前用户 ID，如果不在 UserWorksViewerFragment 中，返回 null
     */
    fun getCurrentUserId(): String?
    
    /**
     * 获取当前视频列表
     * @return 当前视频列表，如果不在 UserWorksViewerFragment 中，返回 null
     */
    fun getCurrentVideoList(): List<VideoItem>?
    
    /**
     * 获取当前视频索引
     * @return 当前视频索引，如果不在 UserWorksViewerFragment 中，返回 null
     */
    fun getCurrentVideoIndex(): Int?
    
    /**
     * 通知进入横屏模式（供 VideoItemFragment 调用，确保按钮横屏和自然横屏逻辑一致）
     */
    fun notifyEnterLandscapeMode()
    
    /**
     * 通知退出横屏模式（供 VideoItemFragment 或导航监听器调用，确保按钮退出和自然横屏退出逻辑一致）
     */
    fun notifyExitLandscapeMode()
}

