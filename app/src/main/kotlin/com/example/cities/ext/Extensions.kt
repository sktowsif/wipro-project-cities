package com.example.cities.ext

import androidx.annotation.StringRes
import com.example.cities.R
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

inline fun Throwable.message(@StringRes msgResId: Int = R.string.err_something_wrong) =
    when (this) {
        is ConnectException -> R.string.err_no_internet
        is SocketTimeoutException -> R.string.err_no_internet
        is TimeoutException -> R.string.err_no_internet
        is UnknownHostException -> R.string.err_no_internet
        else -> msgResId
    }