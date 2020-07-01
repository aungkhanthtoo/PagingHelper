package com.dev.droid.data.network.livedatacalladapter

/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import androidx.lifecycle.LiveData
import retrofit2.*
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A Retrofit adapter that converts the Call into a LiveData of LiveDataResponse.
 * @param <R>
 */
class LiveDataCallAdapter<R>(private val responseType: Type) : CallAdapter<R, LiveData<LiveDataResponse<R>>> {

    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(call: Call<R>): LiveData<LiveDataResponse<R>> {
        return object : LiveData<LiveDataResponse<R>>() {
            var started = AtomicBoolean(false)

            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    call.enqueue(object : Callback<R> {
                        override fun onResponse(call: Call<R>, response: Response<R>) {
                            if (response.isSuccessful) {
                                postValue(LiveDataResponse(response.body(), null))
                            } else {
                                postValue(LiveDataResponse(null, HttpException(response)))
                            }
                        }

                        override fun onFailure(call: Call<R>, throwable: Throwable) {
                            postValue(LiveDataResponse(null, throwable))
                        }
                    })
                }
            }
        }
    }
}