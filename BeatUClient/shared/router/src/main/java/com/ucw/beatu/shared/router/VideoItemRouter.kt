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
}

