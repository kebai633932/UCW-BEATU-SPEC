package com.ucw.beatu.business.search.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.ucw.beatu.business.search.presentation.R
import com.ucw.beatu.business.search.presentation.ui.widget.FlowLayout
import com.ucw.beatu.shared.common.navigation.NavigationHelper
import com.ucw.beatu.shared.common.navigation.NavigationIds

/**
 * AI 搜索入口页面
 */
class AiSearchFragment : Fragment() {

    private lateinit var promptFlow: FlowLayout
    private lateinit var historyContainer: LinearLayout
    private lateinit var inputField: EditText
    private lateinit var sendButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_ai_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar: MaterialToolbar = view.findViewById(R.id.toolbar_ai_search)
        toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

        inputField = view.findViewById(R.id.et_ai_query)

        val initialPrompt = arguments?.getString(ARG_AI_QUERY).orEmpty()
        if (initialPrompt.isNotBlank()) {
            inputField.setText(initialPrompt)
            inputField.setSelection(initialPrompt.length)
        }

        renderPrompts()
        renderHistory()

        sendButton.setOnClickListener {
            val query = inputField.text?.toString().orEmpty()
            if (query.isNotBlank()) {
                openAiResult(query)
            }
        }
    }

    private fun renderPrompts() {
        val prompts = listOf(
            "总结视频里的护肤流程",
            "提炼 3 个营销要点",
            "生成健身训练计划",
            "帮我做旅行清单",
            "推荐同类好物"
        )
        promptFlow.removeAllViews()
        val inflater = LayoutInflater.from(requireContext())
        prompts.forEach { prompt ->
            val chip = inflater.inflate(R.layout.item_search_tag, promptFlow, false) as TextView
            chip.text = prompt
            chip.setOnClickListener { openAiResult(prompt) }
            promptFlow.addView(chip)
        }
    }

    private fun renderHistory() {
        val histories = listOf(
            "AI 帮我分析最近的穿搭趋势",
            "整理这位创作者的视频大纲",
            "提取视频里出现的家居单品链接"
        )
        historyContainer.removeAllViews()
        val inflater = LayoutInflater.from(requireContext())
        histories.forEach { title ->
            val itemView = inflater.inflate(R.layout.item_ai_history, historyContainer, false)
            val tvTitle: TextView = itemView.findViewById(R.id.tv_history_title)
            val tvStatus: TextView = itemView.findViewById(R.id.tv_history_status)
            tvTitle.text = title
            tvStatus.isVisible = true
            itemView.setOnClickListener { openAiResult(title) }
            historyContainer.addView(itemView)
        }
    }

    private fun openAiResult(query: String) {
        NavigationHelper.navigateByStringId(
            findNavController(),
            NavigationIds.ACTION_AI_SEARCH_TO_AI_SEARCH_RESULT,
            requireContext()
        )
    }

    companion object {
        private const val ARG_AI_QUERY = "ai_query"
    }
}

