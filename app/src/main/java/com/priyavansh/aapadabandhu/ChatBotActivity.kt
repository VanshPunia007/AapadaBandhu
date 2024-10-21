package com.priyavansh.aapadabandhu

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.priyavansh.aapadabandhu.ChatBot.ChatAdapter
import com.priyavansh.aapadabandhu.ChatBot.ChatBotVM
import com.priyavansh.aapadabandhu.databinding.ActivityChatBotBinding
import com.priyavansh.aapadabandhu.databinding.ActivityMainBinding

class ChatBotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBotBinding
    private val chatViewModel: ChatBotVM by viewModels()
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        // Send button click listener
        binding.sendButton.setOnClickListener {
            val message = binding.messageInput.text.toString()
            if (message.isNotBlank()) {
                chatViewModel.sendMessage(message)
                binding.messageInput.text.clear()
            }
        }

        // Observe chat messages
        chatViewModel.list.observe(this) { chatData ->
            adapter.submitList(chatData)
            binding.chatRecyclerView.scrollToPosition(chatData.size - 1)
        }
    }

    private fun setupRecyclerView() {
        adapter = ChatAdapter()
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.adapter = adapter
    }
}