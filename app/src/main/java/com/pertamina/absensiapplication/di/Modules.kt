package com.pertamina.absensiapplication.di

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.pertamina.absensiapplication.api.PermitApi
import com.pertamina.absensiapplication.repository.PermitRepository
import com.pertamina.absensiapplication.viewmodel.PermitViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    factory<Interceptor> {
        HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { Log.d("API", it) })
            .setLevel(HttpLoggingInterceptor.Level.HEADERS)
    }

    factory { OkHttpClient.Builder().addInterceptor(get()).build() }

    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl("https://sisfo-absensi.firebaseapp.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    factory { get<Retrofit>().create(PermitApi::class.java) }
}

val repositoryModule = module {
    factory { PermitRepository(get()) }
}

val viewModelModule = module {
    viewModel { PermitViewModel(get(), get()) }
}

val firebaseModule = module {
    factory { FirebaseAuth.getInstance() }
}


val appComponent = listOf(networkModule, viewModelModule, repositoryModule, firebaseModule)