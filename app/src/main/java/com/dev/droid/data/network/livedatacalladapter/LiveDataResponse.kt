package com.dev.droid.data.network.livedatacalladapter

import androidx.lifecycle.LiveData

/**
 * Created with love by A.K.HTOO on 28/06/2020,June,2020.
 */
data class LiveDataResponse<T>(
    val response: T?,
    val error: Throwable?
)

val <T> LiveDataResponse<T>.isSuccess: Boolean
    get() = response != null

typealias ResponseLiveData<T> = LiveData<LiveDataResponse<T>>