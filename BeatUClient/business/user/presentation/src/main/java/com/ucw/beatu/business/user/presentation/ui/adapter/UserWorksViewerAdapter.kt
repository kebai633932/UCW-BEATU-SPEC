package com.ucw.beatu.business.user.presentation.ui.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ucw.beatu.shared.common.model.VideoItem
import com.ucw.beatu.shared.router.RouterRegistry

/**
 * 个人主页作品观看页的 ViewPager2 适配器，复用 VideoItemFragment。
 * 使用反射创建 Fragment，避免编译时直接依赖，解决循环依赖问题。
 */
class UserWorksViewerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    private val videoItems = mutableListOf<VideoItem>()

    override fun getItemCount(): Int = videoItems.size

    override fun createFragment(position: Int): Fragment {
        val item = videoItems[position]
        // 使用 Router 接口创建 VideoItemFragment，避免编译时直接依赖
        val router = RouterRegistry.getVideoItemRouter()
        return if (router != null) {
            router.createVideoItemFragment(item)
        } else {
            Log.e("UserWorksViewerAdapter", "VideoItemRouter not registered")
            Fragment() // fallback
        }
    }

    fun submitList(newList: List<VideoItem>) {
        videoItems.clear()
        videoItems.addAll(newList)
        notifyDataSetChanged()
    }

    fun getVideoAt(position: Int): VideoItem? = videoItems.getOrNull(position)
}


