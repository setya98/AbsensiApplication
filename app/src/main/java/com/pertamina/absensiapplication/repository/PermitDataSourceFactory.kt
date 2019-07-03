package com.pertamina.absensiapplication.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.pertamina.absensiapplication.model.Permit
import kotlinx.coroutines.CoroutineScope

class PermitDataSourceFactory(
    private val repository: PermitRepository,
    private val uid: String = "",
    private val scope: CoroutineScope
) : DataSource.Factory<Int, Permit>() {

    val source = MutableLiveData<PermitDataSource>()

    override fun create(): DataSource<Int, Permit> {
        val source = PermitDataSource(repository, uid, scope)
        this.source.postValue(source)
        return source
    }

}