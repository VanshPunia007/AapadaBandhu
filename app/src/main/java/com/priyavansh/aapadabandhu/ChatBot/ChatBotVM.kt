package com.priyavansh.aapadabandhu.ChatBot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class ChatBotVM: ViewModel() {
    private val _list = MutableLiveData<List<ChatData>>(mutableListOf())
    val list: LiveData<List<ChatData>> = _list

    private val genAI by lazy {
        GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = "AIzaSyCz6Y6y4XW4APipPMGiHdkgeb9PAKpgx70"
        )
    }

    fun sendMessage(text: String) = viewModelScope.launch {
        val currentList = _list.value?.toMutableList() ?: mutableListOf()

        // Add user message to the list
        currentList.add(ChatData(text, ChatRoleEnum.USER.role))
        _list.postValue(currentList)

        // Send message to API and receive response
        val chat = genAI.startChat()
        val response = chat.sendMessage(content(ChatRoleEnum.USER.role) { text(text) }).text

        // Add AI response to the list
        response?.let {
            currentList.add(ChatData(it, ChatRoleEnum.MODEL.role))
            _list.postValue(currentList)
        }
    }
}