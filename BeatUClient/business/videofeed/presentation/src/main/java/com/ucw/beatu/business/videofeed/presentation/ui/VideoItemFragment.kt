package com.ucw.beatu.business.videofeed.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.BundleCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.ui.PlayerView
import com.ucw.beatu.business.videofeed.presentation.R
import com.ucw.beatu.business.videofeed.presentation.model.VideoItem
import com.ucw.beatu.business.videofeed.presentation.viewmodel.VideoItemViewModel
import com.ucw.beatu.shared.common.navigation.LandscapeLaunchContract
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * 单个视频项 Fragment
 * 用于在 ViewPager2 中显示单个视频
 */
@AndroidEntryPoint
class VideoItemFragment : Fragment() {

    companion object {
        private const val TAG = "VideoItemFragment"
        private const val ARG_VIDEO_ITEM = "video_item"

        fun newInstance(videoItem: VideoItem): VideoItemFragment {
            return VideoItemFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_VIDEO_ITEM, videoItem)
                }
            }
        }
    }

    private val viewModel: VideoItemViewModel by viewModels()
    
    private var playerView: PlayerView? = null
    private var playButton: View? = null
    private var videoItem: VideoItem? = null
    private var hasPreparedPlayer = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        videoItem = arguments?.let { 
            BundleCompat.getParcelable(it, ARG_VIDEO_ITEM, VideoItem::class.java)
        }
        if (videoItem == null) {
            Log.e(TAG, "VideoItem is null!")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.item_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // 初始化视图
        playerView = view.findViewById(R.id.player_view)
        playButton = view.findViewById(R.id.iv_play_button)
        
        // 更新视频信息
        videoItem?.let { item ->
            view.findViewById<android.widget.TextView>(R.id.tv_video_title)?.text = item.title
            view.findViewById<android.widget.TextView>(R.id.tv_channel_name)?.text = item.authorName
            view.findViewById<android.widget.TextView>(R.id.tv_like_count)?.text = item.likeCount.toString()
            view.findViewById<android.widget.TextView>(R.id.tv_comment_count)?.text = item.commentCount.toString()
            view.findViewById<android.widget.TextView>(R.id.tv_favorite_count)?.text = item.favoriteCount.toString()
            view.findViewById<android.widget.TextView>(R.id.tv_share_count)?.text = item.shareCount.toString()
        }
        
        // 观察 ViewModel 状态
        observeViewModel()
        
        // 设置播放按钮点击事件
        playButton?.setOnClickListener {
            viewModel.togglePlayPause()
        }
        
        // 设置底部交互按钮点击事件
        view.findViewById<View>(R.id.iv_like)?.setOnClickListener {
            // TODO: 点赞功能
        }
        
        view.findViewById<View>(R.id.iv_favorite)?.setOnClickListener {
            // TODO: 收藏功能
        }
        
        view.findViewById<View>(R.id.iv_comment)?.setOnClickListener {
            // TODO: 打开评论
        }
        
        view.findViewById<View>(R.id.iv_share)?.setOnClickListener {
            // TODO: 分享功能
        }
        
        view.findViewById<View>(R.id.iv_fullscreen)?.setOnClickListener {
            openLandscapeMode()
        }
        
        // 播放逻辑交由生命周期驱动，只在真正可见时启动
    }
    
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    // 更新播放器显示状态
                    playerView?.visibility = View.VISIBLE
                    
                    // 更新播放按钮显示状态（播放时隐藏，暂停时显示）
                    playButton?.visibility = if (state.isPlaying) {
                        View.GONE
                    } else {
                        View.VISIBLE
                    }
                    
                    // 处理错误
                    state.error?.let { error ->
                        Log.e(TAG, "播放错误: $error")
                    }
                }
            }
        }
    }
    
    private fun startPlaybackIfNeeded(forcePrepare: Boolean = false) {
        if (!isAdded || playerView == null || videoItem == null) {
            Log.w(TAG, "Fragment not ready, skip startPlayback")
            return
        }

        if (!hasPreparedPlayer || forcePrepare) {
            prepareAndPlay()
        } else {
            viewModel.resume()
        }
    }

    private fun prepareAndPlay() {
        val item = videoItem ?: return
        val pv = playerView ?: return

        Log.d(TAG, "Preparing video for playback: ${item.id}")
        viewModel.playVideo(item.id, item.videoUrl)
        viewModel.preparePlayer(item.id, item.videoUrl, pv)
        hasPreparedPlayer = true
    }

    /**
     * 供父级 Fragment/Activity 控制可见性时调用
     */
    fun onParentVisibilityChanged(isVisible: Boolean) {
        if (isVisible) {
            startPlaybackIfNeeded()
        } else if (hasPreparedPlayer) {
            viewModel.pause()
        }
    }
    
    override fun onPause() {
        super.onPause()
        if (hasPreparedPlayer) {
            viewModel.pause()
        }
    }
    
    override fun onResume() {
        super.onResume()
        startPlaybackIfNeeded()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        playerView?.player = null
        viewModel.releaseCurrentPlayer()
        playerView = null
        playButton = null
        hasPreparedPlayer = false
    }

    private fun openLandscapeMode() {
        val item = videoItem ?: return
        val intent = Intent().apply {
            setClassName(requireContext(), LandscapeLaunchContract.ACTIVITY_CLASS_NAME)
            putExtra(LandscapeLaunchContract.EXTRA_VIDEO_ID, item.id)
            putExtra(LandscapeLaunchContract.EXTRA_VIDEO_URL, item.videoUrl)
            putExtra(LandscapeLaunchContract.EXTRA_VIDEO_TITLE, item.title)
            putExtra(LandscapeLaunchContract.EXTRA_VIDEO_AUTHOR, item.authorName)
            putExtra(LandscapeLaunchContract.EXTRA_VIDEO_LIKE, item.likeCount)
            putExtra(LandscapeLaunchContract.EXTRA_VIDEO_COMMENT, item.commentCount)
            putExtra(LandscapeLaunchContract.EXTRA_VIDEO_FAVORITE, item.favoriteCount)
            putExtra(LandscapeLaunchContract.EXTRA_VIDEO_SHARE, item.shareCount)
        }
        runCatching { startActivity(intent) }
            .onFailure { Log.e(TAG, "Failed to open landscape mode", it) }
    }
}

