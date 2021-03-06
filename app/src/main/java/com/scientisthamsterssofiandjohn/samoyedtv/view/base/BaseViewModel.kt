package com.scientisthamsterssofiandjohn.samoyedtv.view.base

import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.scientisthamsterssofiandjohn.samoyedtv.util.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren

open class BaseViewModel: ViewModel() {

    protected val job = SupervisorJob()
    protected val main: CoroutineDispatcher = Dispatchers.Main
    protected val io: CoroutineDispatcher = Dispatchers.IO
    protected val default: CoroutineDispatcher = Dispatchers.Default

    protected var pagedConfig: PagedList.Config = PagedList.Config.Builder()
        .setPageSize(10)
        .setInitialLoadSizeHint(10)
        .setEnablePlaceholders(false)
        .build()

    protected val progressState: SingleLiveEvent<Boolean> = SingleLiveEvent()

    override fun onCleared() {
        job.cancelChildren()
        super.onCleared()
    }
}