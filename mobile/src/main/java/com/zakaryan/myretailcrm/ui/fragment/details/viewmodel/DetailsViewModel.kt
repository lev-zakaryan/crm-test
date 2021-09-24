package com.zakaryan.myretailcrm.ui.fragment.details.viewmodel

import androidx.lifecycle.*
import com.zakaryan.myretailcrm.data.Repository
import com.zakaryan.myretailcrm.data.result.Result
import com.zakaryan.myretailcrm.ui.livedata.nonNull
import com.zakaryan.myretailcrm.ui.viewmodel.GenericViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsViewModel(
    private val savedState: SavedStateHandle,
    private val repository: Repository
) : ViewModel() {

    private val _isOperationInProgress = MutableLiveData<Boolean>(false)
    val isOperationInProgress: LiveData<Boolean> = _isOperationInProgress

    private val _firstNameLiveData = savedState.getLiveData<String>(STATE_NAME)
    val firstNameLiveData: LiveData<String> = _firstNameLiveData.nonNull()

    private val _lastNameLiveData = savedState.getLiveData<String>(STATE_SURNAME)
    val lastNameLiveData: LiveData<String> = _lastNameLiveData.nonNull()

    private val _patronymicLiveData = savedState.getLiveData<String>(STATE_PATRONYMIC)
    val patronymicLiveData: LiveData<String> = _patronymicLiveData.nonNull()

    private var _isLoaded = savedState.get(STATE_IS_LOADED) ?: false
        set(value) {
            field = value
            savedState.set(STATE_IS_LOADED, value)
        }
    val isLoaded: Boolean get() = _isLoaded

    fun loadCustomerDetails(userId: String) {
        _isOperationInProgress.value = true
        viewModelScope.launch {
            when (val result = repository.loadCustomerDetails(userId)) {
                is Result.Success -> {
                    _firstNameLiveData.value = result.value.firstName
                    _lastNameLiveData.value = result.value.lastName
                    _patronymicLiveData.value = result.value.patronymic
                    _isLoaded = true
                }
                else -> {
                    // обработка ошибок
                }
            }
            _isOperationInProgress.postValue(false)
        }
    }

    /* ------------------------------- FACTORY -------------------------------------------------- */

    class Factory @Inject constructor(private val repository: Repository) :
        GenericViewModelFactory<DetailsViewModel> {
        override fun create(state: SavedStateHandle): DetailsViewModel {
            return DetailsViewModel(state, repository)
        }
    }

    /* ------------------------------- COMPANION ------------------------------------------------ */

    companion object {
        private const val STATE_IS_LOADED = "STATE_IS_LOADED"
        private const val STATE_NAME = "STATE_NAME"
        private const val STATE_SURNAME = "STATE_SURNAME"
        private const val STATE_PATRONYMIC = "STATE_PATRONYMIC"
    }

}