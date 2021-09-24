package com.zakaryan.myretailcrm.ui.activity.viewmodel

import androidx.lifecycle.*
import com.zakaryan.myretailcrm.data.http.AuthManager
import com.zakaryan.myretailcrm.ui.viewmodel.GenericViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel(
    private val savedState: SavedStateHandle,
    private val authManager: AuthManager
) : ViewModel() {

    private val _isOperationInProgress = MutableLiveData<Boolean>(false)
    val isOperationInProgress: LiveData<Boolean> = _isOperationInProgress

    val isAuthorizedLiveData: LiveData<Boolean> = authManager.authState

    init {
        if (authManager.authState.value == null) {
            _isOperationInProgress.value = true
            viewModelScope.launch {
                authManager.loadTokenCache()
                _isOperationInProgress.value = false
            }
        }
    }

    fun clearSession() {
        _isOperationInProgress.value = true
        viewModelScope.launch {
            authManager.clearTokenCache()
            _isOperationInProgress.value = false
        }
    }

    /* ------------------------------- FACTORY -------------------------------------------------- */

    class Factory @Inject constructor(private val authManager: AuthManager) :
        GenericViewModelFactory<MainViewModel> {
        override fun create(state: SavedStateHandle): MainViewModel {
            return MainViewModel(state, authManager)
        }
    }

}