package com.priyavansh.aapadabandhu

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
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

        // back button
        binding.backBtn.setOnClickListener {
            finish()
        }

        // Send button click listener
        binding.sendButton.setOnClickListener {
            val message = binding.messageInput.text.toString()
            if (message.isNotBlank()) {
                chatViewModel.sendMessage(message)
                binding.messageInput.text.clear()
            }
            // Close the keyboard
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.messageInput.windowToken, 0)
        }

        // Observe chat messages
        chatViewModel.list.observe(this) { chatData ->
            adapter.submitList(chatData)
            binding.chatRecyclerView.scrollToPosition(chatData.size - 1)
            if (chatData.isNotEmpty()) {
                binding.appLogo.visibility = View.GONE
                binding.startupText.visibility = View.GONE
                binding.chatRecyclerView.visibility = View.VISIBLE
            } else {
                binding.appLogo.visibility = View.VISIBLE
                binding.startupText.visibility = View.VISIBLE
                binding.chatRecyclerView.visibility = View.GONE
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = ChatAdapter()
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.adapter = adapter
    }
}