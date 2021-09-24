package com.zakaryan.myretailcrm.ui.fragment.auth.viewmodel

import androidx.lifecycle.*
import com.zakaryan.myretailcrm.data.Repository
import com.zakaryan.myretailcrm.data.result.Result
import com.zakaryan.myretailcrm.ui.fragment.list.viewmodel.ListViewModel
import com.zakaryan.myretailcrm.ui.livedata.SingleEvent
import com.zakaryan.myretailcrm.ui.viewmodel.GenericViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel(
    private val savedState: SavedStateHandle,
    private val repository: Repository
) : ViewModel() {

    private val _isOperationInProgress = MutableLiveData<Boolean>(false)
    val isOperationInProgress: LiveData<Boolean> = _isOperationInProgress

    private val _authResultEvent = SingleEvent<Result<*>>()
    val authResultEvent: LiveData<Result<*>> = _authResultEvent


    fun auth(clientUrl: String, user: String, pass: String) {
        _isOperationInProgress.value = true
        viewModelScope.launch {
            _authResultEvent.postValue(repository.auth(clientUrl, user, pass))
            _isOperationInProgress.value = false
        }
    }

    /* ------------------------------- FACTORY -------------------------------------------------- */

    class Factory @Inject constructor(private val repository: Repository) :
        GenericViewModelFactory<AuthViewModel> {
        override fun create(state: SavedStateHandle): AuthViewModel {
            return AuthViewModel(state, repository)
        }
    }

}