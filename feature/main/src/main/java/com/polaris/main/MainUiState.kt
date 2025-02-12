package com.polaris.main

sealed class MainUiState {
    object ClipboardList : MainUiState()
    object ClipboardSaved : MainUiState()
}