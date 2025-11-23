package com.ucw.beatu.business.settings.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ucw.beatu.business.settings.presentation.R
import com.ucw.beatu.shared.common.navigation.NavigationHelper
import com.ucw.beatu.shared.common.navigation.NavigationIds

/**
 * 设置页面Fragment
 * 显示应用设置选项
 */
class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // 设置返回按钮
        view.findViewById<View>(R.id.btn_back)?.setOnClickListener {
            navigateBack()
        }
        
        // TODO: 添加设置项点击事件
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

