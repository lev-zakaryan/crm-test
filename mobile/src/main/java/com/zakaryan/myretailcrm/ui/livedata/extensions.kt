package com.zakaryan.myretailcrm.ui.livedata

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

@MainThread
@Suppress("NOTHING_TO_INLINE")
inline fun <X> LiveData<X>.nonNull(): LiveData<X> {
    val outputLiveData = MediatorLiveData<X>()
    outputLiveData.addSource(this) { value ->
        if (value != null) {
            outputLiveData.value = value
        }
    }
    return outputLiveData
}

