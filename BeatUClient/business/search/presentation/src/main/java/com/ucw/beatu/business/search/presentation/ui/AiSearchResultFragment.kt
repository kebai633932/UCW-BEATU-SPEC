package com.ucw.beatu.business.search.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.ucw.beatu.business.search.presentation.R

/**
 * AI 搜索结果页面：展示对话
 */
class AiSearchResultFragment : Fragment(R.layout.fragment_ai_search_result) {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var adapter: AiChatAdapter
    private lateinit var followUpInput: EditText
    private lateinit var followUpButton: MaterialButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: MaterialToolbar = view.findViewById(R.id.toolbar_ai_search_result)
        toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

        chatRecyclerView = view.findViewById(R.id.rv_ai_chat)
        adapter = AiChatAdapter()
        chatRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        chatRecyclerView.adapter = adapter

        // 如果从 AiSearchFragment 带了首个提问过来，这里直接渲染成首轮对话
        val initialQuery = arguments?.getString(ARG_AI_QUERY).orEmpty()
        if (initialQuery.isNotBlank()) {
            adapter.addMessage(AiChatMessage.User(initialQuery))
            simulateAiReply(initialQuery)
        }

        followUpInput = view.findViewById(R.id.et_ai_follow_up)
        followUpButton = view.findViewById(R.id.btn_ai_follow_up)
        followUpButton.setOnClickListener {
            val message = followUpInput.text.toString().trim()
            if (message.isNotEmpty()) {
                adapter.addMessage(AiChatMessage.User(message))
                followUpInput.text.clear()
                simulateAiReply(message)
            }
        }
    }

    private fun simulateAiReply(userMessage: String) {
        val reply = "AI 回复: $userMessage"
        adapter.addMessage(AiChatMessage.Ai(reply))
        chatRecyclerView.scrollToPosition(adapter.itemCount - 1)
    }

    companion object {
        const val ARG_AI_QUERY = "ai_query"
    }
}
sealed class AiChatMessage {
    data class User(val content: String) : AiChatMessage()
    data class Ai(val content: String) : AiChatMessage()
}

class AiChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val messages = mutableListOf<AiChatMessage>()

    fun addMessage(message: AiChatMessage) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    override fun getItemViewType(position: Int): Int {
        return when (messages[position]) {
            is AiChatMessage.User -> 0
            is AiChatMessage.Ai -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ai_chat_user, parent, false)
            UserViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ai_chat_ai, parent, false)
            AiViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is UserViewHolder && message is AiChatMessage.User) {
            holder.bind(message)
        } else if (holder is AiViewHolder && message is AiChatMessage.Ai) {
            holder.bind(message)
        }
    }

    override fun getItemCount() = messages.size

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvContent: TextView = view.findViewById(R.id.tv_user_message)
        fun bind(message: AiChatMessage.User) {
            tvContent.text = message.content
        }
    }

    class AiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvContent: TextView = view.findViewById(R.id.tv_ai_message)
        fun bind(message: AiChatMessage.Ai) {
            tvContent.text = message.content
        }
    }
}


