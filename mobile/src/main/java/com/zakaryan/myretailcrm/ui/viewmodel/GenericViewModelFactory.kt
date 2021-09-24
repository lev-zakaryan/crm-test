package com.zakaryan.myretailcrm.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

interface GenericViewModelFactory<out VM : ViewModel> {
    fun create(state: SavedStateHandle): VM
}