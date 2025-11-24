package com.ucw.beatu.business.landscape.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ucw.beatu.business.landscape.presentation.R

/**
 * 横屏页面Fragment
 * 显示横屏视频播放界面
 */
class LandscapeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_landscape, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // 设置返回按钮
        view.findViewById<View>(R.id.btn_back)?.setOnClickListener {
            navigateBack()
        }
        
        // TODO: 实现横屏播放器逻辑
    }
    
    /**
     * 返回上一页
     */
    private fun navigateBack() {
        try {
            if (!findNavController().popBackStack()) {
                // 如果无法返回，则使用 onBackPressedDispatcher
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        } catch (e: Exception) {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}

