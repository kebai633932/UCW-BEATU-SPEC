package com.ucw.beatu.business.videofeed.presentation.router

import androidx.fragment.app.Fragment
import com.ucw.beatu.business.videofeed.presentation.ui.VideoItemFragment
import com.ucw.beatu.shared.common.model.VideoItem
import com.ucw.beatu.shared.router.VideoItemRouter

/**
 * VideoItemRouter 的实现
 * 将 VideoItemFragment 的创建和方法调用逻辑封装为 Router 接口实现
 */
class VideoItemRouterImpl : VideoItemRouter {
    override fun createVideoItemFragment(videoItem: VideoItem): Fragment {
        return VideoItemFragment.newInstance(videoItem)
    }
    
    override fun checkVisibilityAndPlay(fragment: Fragment) {
        if (fragment is VideoItemFragment) {
            fragment.checkVisibilityAndPlay()
        }
    }
    
    override fun onParentVisibilityChanged(fragment: Fragment, isVisible: Boolean) {
        if (fragment is VideoItemFragment) {
            fragment.onParentVisibilityChanged(isVisible)
        }
    }
    
    override fun restorePlayerFromLandscape(fragment: Fragment) {
        if (fragment is VideoItemFragment) {
            fragment.restorePlayerFromLandscape()
        }
    }
    
    override fun openLandscapeMode(fragment: Fragment, videoList: List<VideoItem>?, currentIndex: Int?) {
        if (fragment is VideoItemFragment) {
            fragment.openLandscapeMode(videoList, currentIndex)
        }
    }
}

