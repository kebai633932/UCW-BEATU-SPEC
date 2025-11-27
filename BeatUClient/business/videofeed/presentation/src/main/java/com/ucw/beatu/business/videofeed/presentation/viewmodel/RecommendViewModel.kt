package com.ucw.beatu.business.videofeed.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ucw.beatu.business.videofeed.domain.usecase.GetFeedUseCase
import com.ucw.beatu.business.videofeed.presentation.mapper.toVideoItem
import com.ucw.beatu.business.videofeed.presentation.model.VideoItem
import com.ucw.beatu.shared.common.result.AppResult
import com.ucw.beatu.shared.player.VideoPlayer
import com.ucw.beatu.shared.player.model.VideoSource
import com.ucw.beatu.shared.player.pool.VideoPlayerPool
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 推荐页 ViewModel
 * 管理视频列表、播放器生命周期和状态
 */
data class RecommendUiState(
    val videoList: List<VideoItem> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class RecommendViewModel @Inject constructor(
    application: Application,
    private val playerPool: VideoPlayerPool,
    private val getFeedUseCase: GetFeedUseCase
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(RecommendUiState())
    val uiState: StateFlow<RecommendUiState> = _uiState.asStateFlow()

    private var currentPlayer: VideoPlayer? = null
    private var currentVideoId: String? = null
    private var currentPage = 1
    private val pageSize = 20
    
    init {
        // 初始化时加载视频列表
        loadVideoList()
    }

    /**
     * 加载视频列表（使用GetFeedUseCase）
     */
    private fun loadVideoList() {
        viewModelScope.launch {
            currentPage = 1
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            getFeedUseCase(currentPage, pageSize)
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "加载失败"
                    )
                }
                .collect { result ->
                    when (result) {
                        is AppResult.Loading -> {
                            _uiState.value = _uiState.value.copy(isLoading = true)
                        }
                        is AppResult.Success -> {
                            val videos = result.data.map { it.toVideoItem() }
                            _uiState.value = _uiState.value.copy(
                                videoList = videos,
                                isLoading = false,
                                error = null
                            )
                        }
                        is AppResult.Error -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = result.message ?: result.throwable.message ?: "加载失败"
                            )
                        }
                    }
                }
        }
    }

    /**
     * 刷新视频列表（下拉刷新）
     */
    fun refreshVideoList() {
        viewModelScope.launch {
            currentPage = 1
            _uiState.value = _uiState.value.copy(isRefreshing = true, error = null)
            
            getFeedUseCase(currentPage, pageSize)
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        error = e.message ?: "刷新失败"
                    )
                }
                .collect { result ->
                    when (result) {
                        is AppResult.Loading -> {
                            // Loading状态已在开始时设置
                        }
                        is AppResult.Success -> {
                            val videos = result.data.map { it.toVideoItem() }
                            _uiState.value = _uiState.value.copy(
                                videoList = videos,
                                isRefreshing = false,
                                error = null
                            )
                        }
                        is AppResult.Error -> {
                            _uiState.value = _uiState.value.copy(
                                isRefreshing = false,
                                error = result.message ?: result.throwable.message ?: "刷新失败"
                            )
                        }
                    }
                }
        }
    }

    /**
     * 加载更多视频（上拉加载）
     */
    fun loadMoreVideos() {
        viewModelScope.launch {
            // 避免重复加载
            if (_uiState.value.isLoading) return@launch
            
            currentPage++
            getFeedUseCase(currentPage, pageSize)
                .catch { e ->
                    currentPage-- // 回退页码
                    _uiState.value = _uiState.value.copy(
                        error = e.message ?: "加载更多失败"
                    )
                }
                .collect { result ->
                    when (result) {
                        is AppResult.Loading -> {
                            // 加载更多时不显示全局loading
                        }
                        is AppResult.Success -> {
                            val moreVideos = result.data.map { it.toVideoItem() }
                            val currentList = _uiState.value.videoList.toMutableList()
                            currentList.addAll(moreVideos)
                            
                            _uiState.value = _uiState.value.copy(
                                videoList = currentList,
                                error = null
                            )
                        }
                        is AppResult.Error -> {
                            currentPage-- // 回退页码
                            _uiState.value = _uiState.value.copy(
                                error = result.message ?: result.throwable.message ?: "加载更多失败"
                            )
                        }
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        releaseCurrentPlayer()
    }
    
    private fun releaseCurrentPlayer() {
        currentVideoId?.let { videoId ->
            playerPool.release(videoId)
        }
        currentPlayer = null
        currentVideoId = null
    }


    fun restoreState(videos: List<VideoItem>, restoredPage: Int) {
        currentPage = restoredPage.coerceAtLeast(1)
        _uiState.value = _uiState.value.copy(
            videoList = videos,
            isLoading = false,
            isRefreshing = false,
            error = null
        )
    }

    fun getCurrentPage(): Int = currentPage
}

