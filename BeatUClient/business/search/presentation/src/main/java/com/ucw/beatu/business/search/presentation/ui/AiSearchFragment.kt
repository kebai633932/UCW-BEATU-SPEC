package com.ucw.beatu.business.search.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.ucw.beatu.business.search.presentation.R
import com.ucw.beatu.shared.common.navigation.NavigationHelper
import com.ucw.beatu.shared.common.navigation.NavigationIds

/**
 * AI 搜索入口页面：只负责输入问题并跳转到 AI 对话结果页
 */
class AiSearchFragment : Fragment() {

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
        sendButton = view.findViewById(R.id.btn_ai_follow_up)

        val initialPrompt = arguments?.getString(ARG_AI_QUERY).orEmpty()
        if (initialPrompt.isNotBlank()) {
            inputField.setText(initialPrompt)
            inputField.setSelection(initialPrompt.length)
        }

        sendButton.setOnClickListener {
            val query = inputField.text?.toString().orEmpty()
            if (query.isNotBlank()) {
                openAiResult(query)
            }
        }
    }

    private fun openAiResult(query: String) {
        val args = bundleOf(ARG_AI_QUERY to query)
        val navController = findNavController()
        val context = requireContext()
        val actionId = NavigationHelper.getResourceId(
            context,
            NavigationIds.ACTION_AI_SEARCH_TO_AI_SEARCH_RESULT
        )
        if (actionId != 0) {
            navController.navigate(actionId, args)
        } else {
            navController.navigateUp()
        }
    }

    companion object {
        private const val ARG_AI_QUERY = "ai_query"
    }
}

