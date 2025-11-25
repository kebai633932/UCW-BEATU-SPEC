package com.ucw.beatu.business.landscape.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ucw.beatu.business.landscape.presentation.model.VideoItem
import com.ucw.beatu.business.landscape.presentation.model.VideoOrientation
import com.ucw.beatu.shared.common.mock.MockVideoCatalog
import com.ucw.beatu.shared.common.mock.MockVideoCatalog.Orientation.LANDSCAPE
import com.ucw.beatu.shared.common.mock.Video
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 横屏页 ViewModel
 * 管理横屏视频列表，使用 Repository 获取数据（目前使用 mock 数据）
 */
@HiltViewModel
class LandscapeViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(LandscapeUiState())
    val uiState: StateFlow<LandscapeUiState> = _uiState.asStateFlow()

    private var currentPage = 1
    private val pageSize = 5
    private val maxCachedItems = 40
    private var pendingExternalVideo: VideoItem? = null
    private var shouldReapplyExternalVideo = false
    private var isDefaultListLoading = false

    /**
     * 加载第一页 Mock 数据
     */
    fun loadVideoList() {
        viewModelScope.launch {
            currentPage = 1
            isDefaultListLoading = true
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val mockVideos = MockVideoCatalog.getPage(LANDSCAPE, currentPage, pageSize)
                .map { it.toLandscapeVideoItem() }
            _uiState.value = _uiState.value.copy(
                videoList = mockVideos,
                isLoading = false,
                error = null
            )
            isDefaultListLoading = false
            applyPendingExternalVideo(forceInsert = false)
        }
    }

    /**
     * 加载更多 Mock 数据（复用阿里云链接，保证有效性）
     */
    fun loadMoreVideos() {
        viewModelScope.launch {
            currentPage++
            val moreMockVideos = MockVideoCatalog.getPage(LANDSCAPE, currentPage, pageSize)
                .map { it.toLandscapeVideoItem() }
            val mergedList = (_uiState.value.videoList + moreMockVideos)
                .takeLast(maxCachedItems)
            _uiState.value = _uiState.value.copy(
                videoList = mergedList,
                error = null
            )
        }
    }

    /**
     * 插入外部（竖屏传入）的当前视频，确保横屏页首条就是当前播放视频。
     */
    fun showExternalVideo(videoItem: VideoItem) {
        viewModelScope.launch {
            pendingExternalVideo = videoItem.copy(orientation = VideoOrientation.LANDSCAPE)
            shouldReapplyExternalVideo = isDefaultListLoading || _uiState.value.videoList.isEmpty()
            applyPendingExternalVideo(forceInsert = true)
            if (!shouldReapplyExternalVideo) {
                pendingExternalVideo = null
            }
        }
    }

    /**
     * 将待插入的外部视频放到列表首位。
     * @param forceInsert 当默认列表尚未加载完成但需要立即展示时强制插入
     */
    private fun applyPendingExternalVideo(forceInsert: Boolean) {
        val external = pendingExternalVideo ?: return
        val currentList = _uiState.value.videoList
        if (currentList.isEmpty() && !forceInsert) {
            // 等待默认列表加载完成后再插入
            return
        }

        val mergedList = buildList {
            add(external)
            currentList.forEach { item ->
                if (item.id != external.id) {
                    add(item)
                }
            }
        }.take(maxCachedItems)

        _uiState.value = _uiState.value.copy(
            videoList = mergedList,
            isLoading = false,
            error = null
        )

        if (!forceInsert || !shouldReapplyExternalVideo) {
            pendingExternalVideo = null
            shouldReapplyExternalVideo = false
        }
    }

    private fun Video.toLandscapeVideoItem(): VideoItem =
        VideoItem(
            id = id,
            videoUrl = url,
            title = title,
            authorName = author,
            likeCount = likeCount,
            commentCount = commentCount,
            favoriteCount = favoriteCount,
            shareCount = shareCount,
            orientation = when (orientation) {
                MockVideoCatalog.Orientation.PORTRAIT -> com.ucw.beatu.business.landscape.presentation.model.VideoOrientation.PORTRAIT
                MockVideoCatalog.Orientation.LANDSCAPE -> com.ucw.beatu.business.landscape.presentation.model.VideoOrientation.LANDSCAPE
            }
        )
}

// 移到文件顶层！让外部（Activity/Adapter）能访问
data class LandscapeUiState(
    val videoList: List<VideoItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)