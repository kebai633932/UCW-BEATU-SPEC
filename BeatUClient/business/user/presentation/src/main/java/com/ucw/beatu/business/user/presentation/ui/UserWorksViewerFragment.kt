package com.ucw.beatu.business.user.presentation.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EdgeEffect
import androidx.core.os.BundleCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.ucw.beatu.business.user.presentation.R
import com.ucw.beatu.business.user.presentation.ui.adapter.UserWorksViewerAdapter
import com.ucw.beatu.business.user.presentation.viewmodel.UserWorksViewerViewModel

import com.ucw.beatu.shared.common.model.VideoItem
import com.ucw.beatu.shared.router.UserWorksViewerRouter
import com.ucw.beatu.shared.router.RouterRegistry
import com.ucw.beatu.shared.designsystem.widget.NoMoreVideosToast
import dagger.hilt.android.AndroidEntryPoint
import android.util.Log
import kotlin.math.max
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserWorksViewerFragment : Fragment(R.layout.fragment_user_works_viewer), UserWorksViewerRouter {
    private val TAG = javaClass.simpleName
    private val viewModel: UserWorksViewerViewModel by viewModels()

    private var viewPager: ViewPager2? = null
    private var adapter: UserWorksViewerAdapter? = null
    private var noMoreVideosToast: NoMoreVideosToast? = null
    private var previousDestinationId: Int? = null
    private var isLandscapeMode = false // 跟踪是否在横屏模式
    private var pendingRestoreFromLandscape = false // 标记是否需要从横屏恢复播放器
    private var fragmentLifecycleCallback: FragmentManager.FragmentLifecycleCallbacks? = null // Fragment 生命周期回调
    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            val total = adapter?.itemCount ?: 0
            if (total == 0) return
            val bounded = position.coerceIn(0, total - 1)
            if (position != bounded) {
                viewPager?.setCurrentItem(bounded, false)
                return
            }
            viewModel.updateCurrentIndex(bounded)
            handlePageSelected(bounded)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(view)
        setupViewPager(view)
        parseArgumentsIfNeeded(requireArguments())
        collectUiState()
        // 注册 Router，供子 Fragment 使用
        RouterRegistry.registerUserWorksViewerRouter(this)
        // 设置导航监听，处理从横屏返回的情况
        setupNavigationListener()
        // ✅ 添加 Fragment 生命周期监听，在 Fragment attach 时恢复播放器
        setupFragmentLifecycleCallback()
    }
    
    override fun onResume() {
        super.onResume()
        // ✅ 修复：添加与 RecommendFragment 相同的 onResume 检查机制
        // 检查屏幕方向，如果从横屏返回，标记需要恢复播放器（但不立即恢复，等待 Fragment 创建完成）
        val configuration = resources.configuration
        val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        Log.d(TAG, "onResume: 检查屏幕方向，isPortrait=$isPortrait, isLandscapeMode=$isLandscapeMode")
        if (isPortrait && isLandscapeMode) {
            Log.d(TAG, "onResume: ✅ 检测到从横屏返回，标记需要恢复播放器（等待 Fragment 创建完成）")
            // 标记需要恢复，但不立即恢复，等待 Fragment 创建完成
            pendingRestoreFromLandscape = true
            isLandscapeMode = false
        } else {
            Log.d(TAG, "onResume: 未触发恢复，isPortrait=$isPortrait, isLandscapeMode=$isLandscapeMode")
        }
    }
    
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // ✅ 修复：添加与 RecommendFragment 相同的 onConfigurationChanged 监听机制
        // 由于 MainActivity 配置了 configChanges，屏幕旋转时会调用此方法
        // 使用 post 延迟执行，避免阻塞主线程
        val isLandscape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
        val isPortrait = newConfig.orientation == Configuration.ORIENTATION_PORTRAIT
        Log.d(TAG, "onConfigurationChanged: 屏幕方向变化，isLandscape=$isLandscape, isPortrait=$isPortrait, isLandscapeMode=$isLandscapeMode")
        
        if (isPortrait && isLandscapeMode) {
            // 从横屏返回竖屏，标记需要恢复播放器（等待 Fragment 创建完成）
            Log.d(TAG, "onConfigurationChanged: ✅ 从横屏返回竖屏，标记需要恢复播放器（等待 Fragment 创建完成）")
            pendingRestoreFromLandscape = true
            isLandscapeMode = false
        } else {
            Log.d(TAG, "onConfigurationChanged: 未触发恢复，isPortrait=$isPortrait, isLandscapeMode=$isLandscapeMode")
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        viewPager?.unregisterOnPageChangeCallback(pageChangeCallback)
        viewPager = null
        adapter = null
        // 取消注册 Fragment 生命周期监听
        fragmentLifecycleCallback?.let {
            childFragmentManager.unregisterFragmentLifecycleCallbacks(it)
            fragmentLifecycleCallback = null
        }
        // 取消注册 Router
        RouterRegistry.registerUserWorksViewerRouter(null)
    }
    
    // UserWorksViewerRouter 接口实现
    override fun switchToVideo(index: Int): Boolean {
        val adapter = this.adapter
        val pager = this.viewPager
        if (adapter == null || pager == null) {
            Log.w(TAG, "Cannot switch video: adapter or viewPager is null")
            return false
        }
        
        val itemCount = adapter.itemCount
        if (itemCount == 0) {
            Log.w(TAG, "Cannot switch video: video list is empty")
            return false
        }
        
        val boundedIndex = index.coerceIn(0, itemCount - 1)
        val currentIndex = pager.currentItem
        
        // 如果目标索引与当前索引相同，不需要切换
        if (currentIndex == boundedIndex) {
            Log.d(TAG, "Already at video index $boundedIndex, no need to switch")
            return true
        }
        
        // 直接切换 ViewPager2，使用平滑滚动
        // 这会触发 pageChangeCallback，进而更新 ViewModel 的 currentIndex
        pager.setCurrentItem(boundedIndex, true)
        Log.d(TAG, "Switched to video at index $boundedIndex (from $currentIndex)")
        return true
    }
    
    override fun getCurrentUserId(): String? {
        return viewModel.uiState.value.userId.takeIf { it.isNotEmpty() }
    }
    
    override fun getCurrentVideoList(): List<VideoItem>? {
        return viewModel.uiState.value.videoList.takeIf { it.isNotEmpty() }
    }
    
    override fun getCurrentVideoIndex(): Int? {
        return viewModel.uiState.value.currentIndex.takeIf { 
            viewModel.uiState.value.videoList.isNotEmpty() 
        }
    }
    
    /**
     * 通知进入横屏模式（供 VideoItemFragment 调用，确保按钮横屏和自然横屏逻辑一致）
     */
    override fun notifyEnterLandscapeMode() {
        if (!isLandscapeMode) {
            Log.d(TAG, "notifyEnterLandscapeMode: 按钮横屏进入，设置 isLandscapeMode=true")
            isLandscapeMode = true
        }
    }
    
    /**
     * 通知退出横屏模式（供 VideoItemFragment 或导航监听器调用，确保按钮退出和自然横屏退出逻辑一致）
     */
    override fun notifyExitLandscapeMode() {
        if (isLandscapeMode) {
            Log.d(TAG, "notifyExitLandscapeMode: 退出横屏模式，标记需要恢复播放器（等待 Fragment 创建完成）")
            isLandscapeMode = false
            // 标记需要恢复，等待 Fragment 创建完成
            pendingRestoreFromLandscape = true
        }
    }

    private fun setupToolbar(root: View) {
        val toolbar: MaterialToolbar = root.findViewById(R.id.toolbar_user_works)
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupViewPager(root: View) {
        val pager = root.findViewById<ViewPager2>(R.id.vp_user_works)
        adapter = UserWorksViewerAdapter(this)
        pager.adapter = adapter
        pager.orientation = ViewPager2.ORIENTATION_VERTICAL
        pager.offscreenPageLimit = 1
        attachBounceEffect(pager, root)
        pager.registerOnPageChangeCallback(pageChangeCallback)
        viewPager = pager
        
        // 设置提示视图
        setupNoMoreVideosToast(root)
    }
    
    private fun setupNoMoreVideosToast(root: View) {
        val container = root as? ViewGroup ?: run {
            Log.e(TAG, "setupNoMoreVideosToast: root is not a ViewGroup")
            return
        }
        noMoreVideosToast = NoMoreVideosToast(requireContext())
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        container.addView(noMoreVideosToast, layoutParams)
        Log.d(TAG, "setupNoMoreVideosToast: toast added to container, container=${container.javaClass.simpleName}")
    }

    private fun parseArgumentsIfNeeded(bundle: Bundle) {
        val userId = bundle.getString(ARG_USER_ID).orEmpty()
        val initialIndex = bundle.getInt(ARG_INITIAL_INDEX, 0)
        val videos = BundleCompat.getParcelableArrayList(bundle, ARG_VIDEO_LIST, VideoItem::class.java)
            ?: arrayListOf()
        val searchTitle = bundle.getString(ARG_SEARCH_TITLE).orEmpty()
        val sourceTab = bundle.getString(ARG_SOURCE_TAB).orEmpty()

        Log.d(TAG, "parseArgumentsIfNeeded: userId=$userId, initialIndex=$initialIndex, videoListSize=${videos.size}, searchTitle=$searchTitle, sourceTab=$sourceTab")

        videos.forEachIndexed { index, video ->
            Log.d(TAG, "Video[$index]: id=${video.id}, likeCount=${video.likeCount}")
        }

        // Toolbar 标题：优先显示搜索标题，否则使用默认
        val toolbar: MaterialToolbar? = view?.findViewById(R.id.toolbar_user_works)
        if (searchTitle.isNotBlank()) {
            toolbar?.title = searchTitle
        }

        // 允许 userId 为空（搜索模式），仅依赖传入的视频列表
        viewModel.setInitialData(userId, videos, initialIndex)
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    Log.d(TAG, "collectUiState: videoListSize=${state.videoList.size}, currentIndex=${state.currentIndex}")

                    if (state.videoList.isEmpty()) {
                        Log.w(TAG, "collectUiState: video list is empty, nothing to show")
                    }

                    state.videoList.forEachIndexed { index, video ->
                        Log.d(TAG, "UI State Video[$index]: id=${video.id}, likeCount=${video.likeCount}")
                    }

                    // 延迟提交列表，避免 RecyclerView 崩溃
                    viewPager?.post {
                        adapter?.submitList(state.videoList.toList()) // 提交新列表副本更安全

                        val desiredIndex = state.currentIndex.coerceIn(0, max(state.videoList.lastIndex, 0))
                        val current = viewPager?.currentItem ?: -1
                        if (current != desiredIndex) {
                            viewPager?.setCurrentItem(desiredIndex, false)
                        }
                        handlePageSelected(desiredIndex)
                        // ✅ 注意：恢复播放器现在由 FragmentLifecycleCallbacks 处理，不再需要 postDelayed
                    }
                }
            }
        }
    }

    private fun handlePageSelected(position: Int) {
        val fragmentTag = "f$position"
        val currentFragment = childFragmentManager.findFragmentByTag(fragmentTag)

        // 使用 Router 接口调用 VideoItemFragment 的方法，避免编译时直接依赖
        val router = RouterRegistry.getVideoItemRouter()
        if (router != null) {
            childFragmentManager.fragments.forEach { fragment ->
                if (fragment == currentFragment && fragment.isVisible) {
                    router.checkVisibilityAndPlay(fragment)
                } else {
                    router.onParentVisibilityChanged(fragment, false)
                }
            }
            
            // ✅ 修复：如果 Fragment 已可见且需要恢复播放器，立即恢复
            if (pendingRestoreFromLandscape && currentFragment != null && currentFragment.isVisible) {
                Log.d(TAG, "handlePageSelected: Fragment 已可见，立即恢复播放器，position=$position")
                pendingRestoreFromLandscape = false
                router.restorePlayerFromLandscape(currentFragment)
            }
        } else {
            Log.e("UserWorksViewerFragment", "VideoItemRouter not registered")
        }
    }

    companion object {
        const val ARG_USER_ID = "user_id"
        const val ARG_VIDEO_LIST = "video_list"
        const val ARG_INITIAL_INDEX = "initial_index"
        const val ARG_SEARCH_TITLE = "search_title"
        const val ARG_SOURCE_TAB = "source_tab" // works/favorite/like/history/search
    }

    private fun attachBounceEffect(pager: ViewPager2, root: View) {
        val recyclerView = pager.getChildAt(0) as? RecyclerView ?: return
        recyclerView.edgeEffectFactory = object : RecyclerView.EdgeEffectFactory() {
            override fun createEdgeEffect(rv: RecyclerView, direction: Int): EdgeEffect {
                if (direction == DIRECTION_LEFT || direction == DIRECTION_RIGHT) {
                    return super.createEdgeEffect(rv, direction)
                }
                return BounceEdgeEffect(pager, direction, this@UserWorksViewerFragment)
            }
        }
    }

    private class BounceEdgeEffect(
        private val viewPager: ViewPager2,
        private val direction: Int,
        private val fragment: UserWorksViewerFragment
    ) : EdgeEffect(viewPager.context) {

        private var pulling = false
        private var hasShownToast = false
        private val maxTranslationPx =
            viewPager.context.resources.displayMetrics.density * MAX_TRANSLATION_DP

        override fun onPull(deltaDistance: Float) {
            super.onPull(deltaDistance)
            handlePull(deltaDistance)
        }

        override fun onPull(deltaDistance: Float, displacement: Float) {
            super.onPull(deltaDistance, displacement)
            handlePull(deltaDistance)
        }

        override fun onRelease() {
            super.onRelease()
            if (pulling) {
                animateBack()
                pulling = false
                hasShownToast = false
            }
        }

        override fun onAbsorb(velocity: Int) {
            super.onAbsorb(velocity)
            animateBack()
            hasShownToast = false
        }

        private fun handlePull(deltaDistance: Float) {
            val adapter = viewPager.adapter ?: return
            val itemCount = adapter.itemCount
            if (itemCount == 0) return

            val currentItem = viewPager.currentItem
            val isAtTop = currentItem == 0 && direction == RecyclerView.EdgeEffectFactory.DIRECTION_TOP
            val isAtBottom = currentItem == itemCount - 1 && direction == RecyclerView.EdgeEffectFactory.DIRECTION_BOTTOM

            // 如果到达边界，显示提示（降低阈值，确保更容易触发）
            if ((isAtTop || isAtBottom) && !hasShownToast && Math.abs(deltaDistance) > 0.01f) {
                Log.d("BounceEdgeEffect", "Showing no more videos toast: isAtTop=$isAtTop, isAtBottom=$isAtBottom, deltaDistance=$deltaDistance")
                fragment.showNoMoreVideosToast()
                hasShownToast = true
            }

            val sign = if (direction == RecyclerView.EdgeEffectFactory.DIRECTION_TOP) 1 else -1
            val drag = sign * viewPager.height * deltaDistance * 0.6f
            val newTranslation = (viewPager.translationY + drag)
            val clamped = newTranslation.coerceIn(-maxTranslationPx, maxTranslationPx)
            viewPager.translationY = clamped
            pulling = true
        }

        private fun animateBack() {
            viewPager.animate()
                .translationY(0f)
                .setDuration(250L)
                .setInterpolator(android.view.animation.OvershootInterpolator())
                .start()
        }

        companion object {
            private const val MAX_TRANSLATION_DP = 96f
        }
    }
    
    fun showNoMoreVideosToast() {
        Log.d(TAG, "showNoMoreVideosToast called, toast=${noMoreVideosToast != null}")
        noMoreVideosToast?.show()
    }
    
    /**
     * 设置导航监听，处理从横屏返回的情况
     */
    private fun setupNavigationListener() {
        val navController = findNavController()
        
        // 初始化 previousDestinationId 为当前的目标
        previousDestinationId = navController.currentDestination?.id
        
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val userWorksViewerDestinationId = com.ucw.beatu.shared.common.navigation.NavigationHelper.getResourceId(
                requireContext(),
                com.ucw.beatu.shared.common.navigation.NavigationIds.USER_WORKS_VIEWER
            )
            val landscapeDestinationId = com.ucw.beatu.shared.common.navigation.NavigationHelper.getResourceId(
                requireContext(),
                com.ucw.beatu.shared.common.navigation.NavigationIds.LANDSCAPE
            )
            
            // ✅ 修复：当导航到横屏页面时，设置横屏模式标志
            if (destination.id == landscapeDestinationId && previousDestinationId == userWorksViewerDestinationId) {
                Log.d(TAG, "导航到横屏页面，设置 isLandscapeMode=true")
                isLandscapeMode = true
            }
            
            // 当从横屏返回到用户作品观看页面时，标记需要恢复播放器
            if (destination.id == userWorksViewerDestinationId && previousDestinationId == landscapeDestinationId) {
                Log.d(TAG, "从横屏返回到用户作品观看页面，标记需要恢复播放器，previousDestinationId=$previousDestinationId, isLandscapeMode=$isLandscapeMode")
                // ✅ 修复：确保 isLandscapeMode 为 true（应该在进入横屏时已设置，这里作为保险）
                if (!isLandscapeMode) {
                    Log.w(TAG, "警告：从横屏返回时 isLandscapeMode=false，可能未正确设置，强制设置为 true")
                    isLandscapeMode = true
                }
                // 标记需要恢复，等待 Fragment 创建完成
                // 注意：这里作为备用机制，主要依赖 onConfigurationChanged 和 onResume
                pendingRestoreFromLandscape = true
                isLandscapeMode = false
            }
            
            // 记录当前的导航目标，作为下次的前一个目标
            previousDestinationId = destination.id
        }
    }
    
    /**
     * 设置 Fragment 生命周期回调，在 Fragment View 创建时恢复播放器
     * 这是事件驱动的方案，比时间延迟更可靠
     * 使用 onFragmentViewCreated 确保 Fragment 的 View 已创建，可以安全地操作播放器
     */
    private fun setupFragmentLifecycleCallback() {
        fragmentLifecycleCallback = object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentViewCreated(
                fm: FragmentManager,
                f: Fragment,
                v: View,
                savedInstanceState: Bundle?
            ) {
                super.onFragmentViewCreated(fm, f, v, savedInstanceState)
                // 检查是否需要恢复播放器
                if (pendingRestoreFromLandscape) {
                    val currentPosition = viewPager?.currentItem ?: -1
                    if (currentPosition >= 0) {
                        val fragmentTag = "f$currentPosition"
                        // 确保是当前页面的 Fragment
                        if (f.tag == fragmentTag) {
                            // 使用 Router 接口检查 Fragment 类型并恢复播放器
                            val router = RouterRegistry.getVideoItemRouter()
                            if (router != null) {
                                // 使用 post 确保 Fragment 已完全初始化（包括 isVisible 状态）
                                viewPager?.post {
                                    if (f.isVisible && pendingRestoreFromLandscape) {
                                        Log.d(TAG, "onFragmentViewCreated: Fragment View 已创建，恢复播放器，tag=${f.tag}, position=$currentPosition")
                                        pendingRestoreFromLandscape = false
                                        router.restorePlayerFromLandscape(f)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // 注册 Fragment 生命周期回调，递归监听子 Fragment
        childFragmentManager.registerFragmentLifecycleCallbacks(fragmentLifecycleCallback!!, true)
    }
    
    /**
     * 从横屏返回后恢复播放器
     * 使用与 RecommendFragment 相同的逻辑
     * 注意：现在主要由 FragmentLifecycleCallbacks 处理，此方法作为备用
     */
    private fun restorePlayerFromLandscape() {
        val currentPosition = viewPager?.currentItem ?: -1
        Log.d(TAG, "restorePlayerFromLandscape: 开始恢复播放器，currentPosition=$currentPosition, isLandscapeMode=$isLandscapeMode")
        if (currentPosition >= 0) {
            val currentFragmentTag = "f$currentPosition"
            val currentFragment = childFragmentManager.findFragmentByTag(currentFragmentTag)
            
            // 使用 Router 接口调用 VideoItemFragment 的恢复方法
            val router = RouterRegistry.getVideoItemRouter()
            if (router != null && currentFragment != null && currentFragment.isVisible) {
                Log.d(TAG, "restorePlayerFromLandscape: ✅ 找到可见的VideoItemFragment，position=$currentPosition，开始恢复播放器")
                router.restorePlayerFromLandscape(currentFragment)
            } else {
                Log.w(TAG, "restorePlayerFromLandscape: ❌ 未找到可见的VideoItemFragment，currentPosition=$currentPosition, router=${router != null}, fragment=${currentFragment != null}, isVisible=${currentFragment?.isVisible}")
            }
        } else {
            Log.w(TAG, "restorePlayerFromLandscape: ❌ currentPosition 无效，currentPosition=$currentPosition")
        }
    }
}


