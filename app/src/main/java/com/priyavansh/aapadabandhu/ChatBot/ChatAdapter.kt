package com.priyavansh.aapadabandhu.ChatBot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.priyavansh.aapadabandhu.R
import com.priyavansh.aapadabandhu.databinding.ItemMesaageBinding


class ChatAdapter : ListAdapter<ChatData, ChatAdapter.ChatViewHolder>(ChatDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemMesaageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatData = getItem(position)
        holder.bind(chatData)
    }

    class ChatViewHolder(private val binding: ItemMesaageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chatData: ChatData) {
            // Null safety for message and role
            binding.messageText.text = chatData.message.replace("**", "").trimEnd() ?: "No message"

            if (chatData.role == "model") {
                binding.messageText.setBackgroundResource(R.drawable.chatbot_reply_design)
                binding.chatbotLogo.visibility = View.VISIBLE

                // Set constraints for model message
                val params = binding.messageText.layoutParams as ConstraintLayout.LayoutParams
                params.startToEnd = binding.chatbotLogo.id
                params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID // Keep it within screen bounds
                binding.messageText.layoutParams = params
            } else {
                binding.messageText.setBackgroundResource(R.drawable.user_message_design)
                binding.chatbotLogo.visibility = View.GONE

                // Set constraints for user message
                val params = binding.messageText.layoutParams as ConstraintLayout.LayoutParams
                params.startToEnd = ConstraintLayout.LayoutParams.UNSET
                params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                binding.messageText.layoutParams = params
            }

        }
    }
}

class ChatDiffCallback : DiffUtil.ItemCallback<ChatData>() {
    override fun areItemsTheSame(oldItem: ChatData, newItem: ChatData): Boolean {
        // Compare items based on their content. Assuming all ChatData objects are unique.
        return oldItem.message == newItem.message && oldItem.role == newItem.role
    }

    override fun areContentsTheSame(oldItem: ChatData, newItem: ChatData): Boolean {
        return oldItem == newItem
    }
}