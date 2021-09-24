package com.zakaryan.myretailcrm.ui.fragment.list.viewmodel

import androidx.lifecycle.*
import com.zakaryan.myretailcrm.base.http.model.data.Customer
import com.zakaryan.myretailcrm.data.Repository
import com.zakaryan.myretailcrm.data.result.Result
import com.zakaryan.myretailcrm.ui.livedata.nonNull
import com.zakaryan.myretailcrm.ui.viewmodel.GenericViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListViewModel(
    private val savedState: SavedStateHandle,
    private val repository: Repository
) : ViewModel() {

    private val _isOperationInProgress = MutableLiveData(false)
    val isOperationInProgress: LiveData<Boolean> = _isOperationInProgress

    private val _customersListLiveData = savedState.getLiveData<List<Customer>>(STATE_CUSTOMERS)
    val customersListLiveData: LiveData<List<Customer>> = _customersListLiveData.nonNull()

    private var _isLoaded = savedState.get(STATE_IS_LOADED) ?: false
        set(value) {
            field = value
            savedState.set(STATE_IS_LOADED, value)
        }
    val isLoaded: Boolean get() = _isLoaded

    fun loadCustomersList() {
        _isOperationInProgress.value = true
        viewModelScope.launch {
            when (val result = repository.loadCustomersList()) {
                is Result.Success -> {
                    _customersListLiveData.value = result.value
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
        GenericViewModelFactory<ListViewModel> {
        override fun create(state: SavedStateHandle): ListViewModel {
            return ListViewModel(state, repository)
        }
    }

    /* ------------------------------- COMPANION ------------------------------------------------ */

    companion object {
        private const val STATE_IS_LOADED = "STATE_IS_LOADED"
        private const val STATE_CUSTOMERS = "STATE_CUSTOMERS"
    }

}