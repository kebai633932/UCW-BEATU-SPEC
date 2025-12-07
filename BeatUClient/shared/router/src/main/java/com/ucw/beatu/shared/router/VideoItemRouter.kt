package com.ucw.beatu.shared.router

import androidx.fragment.app.Fragment
import com.ucw.beatu.shared.common.model.VideoItem

/**
 * 视频项展示路由接口
 * 用于解耦 videofeed 和 user 模块之间的循环依赖
 * 
 * user 模块依赖此接口，videofeed 模块实现此接口
 */
interface VideoItemRouter {
    /**
     * 创建视频项展示 Fragment
     * @param videoItem 视频项数据
     * @return 视频项展示 Fragment
     */
    fun createVideoItemFragment(videoItem: VideoItem): Fragment
    
    /**
     * 检查 Fragment 是否可见并播放
     * @param fragment Fragment 实例
     */
    fun checkVisibilityAndPlay(fragment: Fragment)
    
    /**
     * 通知 Fragment 父级可见性变化
     * @param fragment Fragment 实例
     * @param isVisible 是否可见
     */
    fun onParentVisibilityChanged(fragment: Fragment, isVisible: Boolean)
    
    /**
     * 从横屏返回后恢复播放器
     * @param fragment Fragment 实例
     */
    fun restorePlayerFromLandscape(fragment: Fragment)
    
    /**
     * 打开横屏模式（支持传递视频列表，用于作品视频页）
     * @param fragment Fragment 实例
     * @param videoList 视频列表（可选，如果提供则横屏页面使用固定列表模式）
     * @param currentIndex 当前视频索引（可选，与 videoList 一起使用）
     */
    fun openLandscapeMode(fragment: Fragment, videoList: List<VideoItem>? = null, currentIndex: Int? = null)
}

