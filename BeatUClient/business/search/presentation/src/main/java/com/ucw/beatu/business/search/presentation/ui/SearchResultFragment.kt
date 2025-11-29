package com.ucw.beatu.business.search.presentation.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ucw.beatu.business.search.presentation.R

/**
 * 常规搜索结果页面
 */
class SearchResultFragment : Fragment() {

    private lateinit var searchEditText: EditText
    private lateinit var clearButton: View
    private lateinit var searchButton: TextView
    private lateinit var backButton: View
    private lateinit var resultAdapter: SearchResultListAdapter
    private var currentQuery: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_search_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentQuery = arguments?.getString(ARG_QUERY).orEmpty()
        setupSearchHeader(view)
        setupResultList(view)
        renderResults(currentQuery)
    }

    private fun setupSearchHeader(view: View) {
        searchEditText = view.findViewById(R.id.et_search)
        clearButton = view.findViewById(R.id.btn_clear)
        searchButton = view.findViewById(R.id.tv_search)
        backButton = view.findViewById(R.id.btn_back)

        searchEditText.setText(currentQuery)
        searchEditText.setSelection(searchEditText.text?.length ?: 0)
        clearButton.isVisible = currentQuery.isNotEmpty()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                triggerSearch(searchEditText.text.toString())
                true
            } else {
                false
            }
        }

        clearButton.setOnClickListener {
            searchEditText.text?.clear()
        }

        searchButton.setOnClickListener {
            triggerSearch(searchEditText.text.toString())
        }

        backButton.setOnClickListener {
            if (!findNavController().popBackStack()) {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun setupResultList(view: View) {
        val rvResults = view.findViewById<RecyclerView>(R.id.rv_search_result_list)
        rvResults.layoutManager = LinearLayoutManager(requireContext())
        resultAdapter = SearchResultListAdapter()
        rvResults.adapter = resultAdapter
    }

    private fun triggerSearch(query: String) {
        if (query.isBlank()) return
        currentQuery = query
        renderResults(query)
    }

    private fun renderResults(query: String) {
        val results = buildMockResults(query)
        resultAdapter.submitList(results)
    }

    private fun buildMockResults(keyword: String): List<SearchResultUiModel> {
        val prefix = if (keyword.isBlank()) "热门" else keyword
        return listOf(
            SearchResultUiModel(
                title = "$prefix · 城市夜游 Vlog",
                description = "3 天 2 夜超详细路线，附航拍镜头与人均预算",
                author = "旅拍小熊",
                duration = "05:12"
            ),
            SearchResultUiModel(
                title = "$prefix · AI 精选好物",
                description = "护肤品测评 + 实测对比，AI 智能匹配肤质",
                author = "元宝实验室",
                duration = "03:48"
            ),
            SearchResultUiModel(
                title = "$prefix · 进阶训练计划",
                description = "针对新手的 7 天训练流程，含饮食建议与动作讲解",
                author = "BeatU Coach",
                duration = "08:20"
            ),
            SearchResultUiModel(
                title = "$prefix · 热门话题讨论",
                description = "邀请三位创作者聊聊最近爆火的趋势，彩蛋在最后",
                author = "话题放映室",
                duration = "04:05"
            )
        )
    }

    data class SearchResultUiModel(
        val title: String,
        val description: String,
        val author: String,
        val duration: String
    )

    private class SearchResultListAdapter :
        RecyclerView.Adapter<SearchResultListAdapter.ResultViewHolder>() {

        private var items: List<SearchResultUiModel> = emptyList()

        fun submitList(newItems: List<SearchResultUiModel>) {
            items = newItems
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_search_result, parent, false)
            return ResultViewHolder(view)
        }

        override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int = items.size

        inner class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val title: TextView = itemView.findViewById(R.id.tv_title)
            private val desc: TextView = itemView.findViewById(R.id.tv_description)
            private val author: TextView = itemView.findViewById(R.id.tv_author)
            private val duration: TextView = itemView.findViewById(R.id.tv_duration)

            fun bind(model: SearchResultUiModel) {
                title.text = model.title
                desc.text = model.description
                author.text = model.author
                duration.text = model.duration
            }
        }
    }

    companion object {
        private const val ARG_QUERY = "search_query"
    }
}

