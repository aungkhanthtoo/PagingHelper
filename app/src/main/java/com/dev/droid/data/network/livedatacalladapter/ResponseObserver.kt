package com.dev.droid.data.network.livedatacalladapter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import java.lang.RuntimeException

/**
 * Created with love by A.K.HTOO on 30/06/2020,June,2020.
 */
abstract class ResponseObserver<T> : Observer<LiveDataResponse<T>> {

    override fun onChanged(t: LiveDataResponse<T>) {
        if (t.isSuccess) {
            onSuccess(t.response!!)
        } else {
            onError(t.error!!.message ?: "Something went wrong!")
        }
    }

    abstract fun onSuccess(data: T)

    abstract fun onError(msg: String)
}

inline fun <T> ResponseLiveData<T>.observe(
    owner: LifecycleOwner,
    crossinline onError: (String) -> Unit,
    crossinline onSuccess: (T) -> Unit
) {
    this.observe(owner, object: ResponseObserver<T>(){
        override fun onSuccess(data: T) {
            onSuccess(data)
        }

        override fun onError(msg: String) {
            onError(msg)
        }

    })
}

inline fun <T> ResponseLiveData<T>.observe(
    owner: LifecycleOwner,
    crossinline onSuccess: (T) -> Unit
) {
    this.observe(
        owner,
        {
            throw RuntimeException("Error not expected! but $it")
        },
        onSuccess)
}