package com.priyavansh.aapadabandhu.ChatBot

sealed interface ChatBotUiState {
    data object Ideal : ChatBotUiState
    data object Loading : ChatBotUiState

    data class Success(val ChatData: String) : ChatBotUiState
    data class Error(val error: String) : ChatBotUiState
}