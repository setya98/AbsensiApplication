package com.pertamina.absensiapplication.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.pertamina.absensiapplication.api.NetworkState
import com.pertamina.absensiapplication.model.User
import kotlinx.coroutines.*

class UserDataSource(
    private val repository: PermitRepository,
    private val scope: CoroutineScope
) : PageKeyedDataSource<Int, User>() {

    private var supervisorJob = SupervisorJob()
    private val networkState = MutableLiveData<NetworkState>()
    private var retryQuery: (() -> Any)? = null

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, User>) {
        retryQuery = { loadInitial(params, callback) }
        getLeagues {
            callback.onResult(it, 0, it.size, null, null)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
        retryQuery = { loadAfter(params, callback) }
        getLeagues {
            callback.onResult(it, null)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {}

    private fun getLeagues(callback: (List<User>) -> Unit) {
        networkState.postValue(NetworkState.RUNNING)
        scope.launch(getJobErrorHandler() + supervisorJob) {
            delay(200)
            val leagues = repository.getUsers()
            retryQuery = null
            networkState.postValue(NetworkState.SUCCESS)
            callback(leagues)
        }
    }

    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        Log.e(UserDataSource::class.java.simpleName, "An error happened: $e")
        networkState.postValue(NetworkState.FAILED)
    }

    override fun invalidate() {
        super.invalidate()
        supervisorJob.cancelChildren()
    }

    fun getNetworkState(): LiveData<NetworkState> = networkState

    fun retryFailedQuery() {
        val prevQuery = retryQuery
        retryQuery = null
        prevQuery?.invoke()
    }
}
