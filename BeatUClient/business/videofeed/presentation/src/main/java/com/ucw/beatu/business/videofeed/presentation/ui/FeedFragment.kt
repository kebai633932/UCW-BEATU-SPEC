package com.ucw.beatu.business.videofeed.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ucw.beatu.business.videofeed.presentation.R

class FeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // 设置顶部导航栏点击事件
        view.findViewById<View>(R.id.btn_follow).setOnClickListener {
            // TODO: 切换到关注页面
        }
        
        view.findViewById<View>(R.id.btn_recommend).setOnClickListener {
            // TODO: 切换到推荐页面
        }
        
        view.findViewById<View>(R.id.btn_me).setOnClickListener {
            // TODO: 切换到个人主页
        }
        
        view.findViewById<View>(R.id.iv_search).setOnClickListener {
            // TODO: 打开搜索页面
        }
        
        // 设置底部交互按钮点击事件
        view.findViewById<View>(R.id.iv_like).setOnClickListener {
            // TODO: 点赞功能
        }
        
        view.findViewById<View>(R.id.iv_favorite).setOnClickListener {
            // TODO: 收藏功能
        }
        
        view.findViewById<View>(R.id.iv_comment).setOnClickListener {
            // TODO: 打开评论
        }
        
        view.findViewById<View>(R.id.iv_share).setOnClickListener {
            // TODO: 分享功能
        }
    }
}

