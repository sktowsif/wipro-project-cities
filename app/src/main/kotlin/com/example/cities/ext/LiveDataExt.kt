package com.example.cities.ext

import androidx.lifecycle.*
import com.example.cities.base.Outcome

inline fun <T> LiveData<T>.reObserve(owner: LifecycleOwner, observer: Observer<T>) {
    removeObserver(observer)
    observe(owner, observer)
}

suspend inline fun <T> LiveDataScope<Outcome<T>>.emitLoading() {
    emit(Outcome.loading(true))
}

suspend inline fun <T> LiveDataScope<Outcome<T>>.emitSuccess(data: T) {
    emit(Outcome.loading(false))
    emit(Outcome.success(data))
}

suspend inline fun <T> LiveDataScope<Outcome<T>>.emitFailure(ex: Throwable) {
    emit(Outcome.loading(false))
    emit(Outcome.failure(ex))
}