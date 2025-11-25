package com.ucw.beatu.business.landscape.presentation.ui

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.ucw.beatu.business.landscape.presentation.R
import com.ucw.beatu.business.landscape.presentation.ui.adapter.LandscapeVideoAdapter
import com.ucw.beatu.business.landscape.presentation.viewmodel.LandscapeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.lifecycle.repeatOnLifecycle
import kotlin.collections.isNotEmpty


/**
 * 横屏播放页面
 * - 全屏、黑色背景
 * - 支持上下滑动切换横屏视频
 * - 左上角退出按钮
 * - 集成 ExoPlayer、手势控制、亮度/音量调节
 */
@AndroidEntryPoint
class LandscapeActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LandscapeActivity"
        const val EXTRA_START_VIDEO_ID = "extra_start_video_id"
        private const val STATE_CURRENT_INDEX = "state_current_index"
    }

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: LandscapeVideoAdapter
    private lateinit var viewModel: LandscapeViewModel
    private var exitButton: ImageButton? = null
    private var pendingVideoId: String? = null
    private var pendingIndex: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 强制横屏
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        
        // 全屏模式（使用新的 API）
        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let { controller ->
                controller.hide(androidx.core.view.WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            )
        }
        
        setContentView(R.layout.activity_landscape)

        pendingVideoId = intent.getStringExtra(EXTRA_START_VIDEO_ID)
        if (savedInstanceState != null) {
            pendingIndex = savedInstanceState.getInt(STATE_CURRENT_INDEX, -1).takeIf { it >= 0 }
            if (pendingVideoId == null) {
                pendingVideoId = savedInstanceState.getString(EXTRA_START_VIDEO_ID)
            }
        }
        
        // 初始化 ViewModel
        viewModel = ViewModelProvider(this)[LandscapeViewModel::class.java]
        
        // 初始化退出按钮
        exitButton = findViewById(R.id.btn_exit_landscape)
        exitButton?.setOnClickListener {
            finish()
        }
        
        // 初始化 ViewPager2
        viewPager = findViewById(R.id.viewpager_landscape)
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
        // 预加载相邻视频（前后各1页）
        viewPager.offscreenPageLimit = 1
        
        // 创建 Adapter
        adapter = LandscapeVideoAdapter(this)
        viewPager.adapter = adapter
        
        // 设置页面切换监听
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d(TAG, "Page selected: $position")
                // 如果滑动到最后一个视频，加载更多
                if (position >= adapter.itemCount - 2) {
                    viewModel.loadMoreVideos()
                }
            }
        })
        
        // 观察 ViewModel 状态
        observeViewModel()
    }
    
    private fun observeViewModel() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state: com.ucw.beatu.business.landscape.presentation.viewmodel.LandscapeUiState ->
                    if (state.videoList.isNotEmpty()) {
                        adapter.updateVideoList(state.videoList)
                        scrollToPendingPosition(state)
                    }
                }
            }
        }
    }

    private fun scrollToPendingPosition(state: com.ucw.beatu.business.landscape.presentation.viewmodel.LandscapeUiState) {
        val targetIndex = when {
            pendingVideoId != null -> state.videoList.indexOfFirst { it.id == pendingVideoId }
            pendingIndex != null -> pendingIndex!!
            else -> -1
        }
        if (targetIndex in 0 until adapter.itemCount) {
            viewPager.post {
                viewPager.setCurrentItem(targetIndex, false)
            }
        }
        pendingVideoId = null
        pendingIndex = null
    }
    
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        // 退出横屏模式
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(STATE_CURRENT_INDEX, viewPager.currentItem)
        pendingVideoId?.let { outState.putString(EXTRA_START_VIDEO_ID, it) }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        pendingIndex = savedInstanceState.getInt(STATE_CURRENT_INDEX, -1).takeIf { it >= 0 }
        if (pendingVideoId == null) {
            pendingVideoId = savedInstanceState.getString(EXTRA_START_VIDEO_ID)
        }
    }
}