package com.pertamina.absensiapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.google.firebase.auth.FirebaseAuth
import com.pertamina.absensiapplication.api.NetworkState
import com.pertamina.absensiapplication.model.Permit
import com.pertamina.absensiapplication.model.User
import com.pertamina.absensiapplication.repository.PermitDataSource
import com.pertamina.absensiapplication.repository.PermitDataSourceFactory
import com.pertamina.absensiapplication.repository.PermitRepository
import com.pertamina.absensiapplication.repository.UserDataSourceFactory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class PermitViewModel(private val repository: PermitRepository, private val auth: FirebaseAuth) : BaseViewModel() {
    private var supervisorJob = SupervisorJob()
    private val permitDataSource = PermitDataSourceFactory(repository, auth.currentUser?.uid.toString(), ioScope)
    val permits = LivePagedListBuilder(permitDataSource, pagedListConfig()).build()
    private val userDataSource = UserDataSourceFactory(repository, ioScope)
    val users = LivePagedListBuilder(userDataSource, pagedListConfig()).build()
    private var networkStatePagedList: LiveData<NetworkState>? = null
    private val user = MutableLiveData<User>()
    private val networkState = MutableLiveData<NetworkState>()

    fun getNetworkHistory(): LiveData<NetworkState>? {
        networkStatePagedList =  switchMap(permitDataSource.source) {
            it.getNetworkState()
        }
        return networkStatePagedList
    }
    fun getNetworkManageUser(): LiveData<NetworkState>? {
        networkStatePagedList =  switchMap(userDataSource.source) {
            it.getNetworkState()
        }
        return networkStatePagedList
    }

    fun getNetwork() = networkState

    fun createPermit(permit: Permit) {
        repository.createPermit(auth.currentUser?.uid.toString(), permit)
    }

    fun createUser(email:String, password:String): MutableLiveData<User> {
        ioScope.launch(getJobErrorHandler() + supervisorJob) {
            val newUser = User(userId = repository.createAccount(email, password))
            user.postValue(newUser)
        }
        return user
    }

    fun createUserData(uid: String, user: User) {
        networkState.postValue(NetworkState.RUNNING)
        ioScope.launch(getJobErrorHandler() + supervisorJob) {
            repository.createUserData(uid, user)
            networkState.postValue(NetworkState.SUCCESS)
        }
    }

    fun getUser(): MutableLiveData<User> {
        networkState.postValue(NetworkState.RUNNING)
        ioScope.launch(getJobErrorHandler() + supervisorJob) {
            user.postValue(repository.getUser(auth.currentUser?.uid.toString()))
            networkState.postValue(NetworkState.SUCCESS)
        }
        return user
    }

    fun getOH(): MutableLiveData<List<User>> {
        networkState.postValue(NetworkState.RUNNING)
        val operationHead =  MutableLiveData<List<User>>()
        ioScope.launch(getJobErrorHandler() + supervisorJob) {
            operationHead.postValue(repository.getOH())
            networkState.postValue(NetworkState.SUCCESS)
        }
        return operationHead
    }

    fun getSenior(): MutableLiveData<List<User>> {
        networkState.postValue(NetworkState.RUNNING)
        val senior =  MutableLiveData<List<User>>()
        ioScope.launch(getJobErrorHandler() + supervisorJob) {
            senior.postValue(repository.getSenior())
            networkState.postValue(NetworkState.SUCCESS)
        }
        return senior
    }

    private fun pagedListConfig() = PagedList.Config.Builder()
        .setInitialLoadSizeHint(7)
        .setEnablePlaceholders(false)
        .setPageSize(7 * 2)
        .build()

    fun refresh() = permitDataSource.source.value?.invalidate()
    fun refreshUsers() = userDataSource.source.value?.invalidate()
    fun refreshUser() = getUser()
    fun refreshOH() = getOH()
    fun refreshSenior() = getSenior()

    fun refreshFailedRequest() =
        permitDataSource.source.value?.retryFailedQuery()

    fun refreshFailedUsersRequest() =
        userDataSource.source.value?.retryFailedQuery()

    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        Log.e(PermitDataSource::class.java.simpleName, "An error happened: $e")
        networkState.postValue(NetworkState.FAILED)
    }
}