package com.priyavansh.aapadabandhu.ChatBot

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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
            binding.messageText.text = chatData.message ?: "No message"
            binding.messageSender.text = chatData.role ?: "Unknown"
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