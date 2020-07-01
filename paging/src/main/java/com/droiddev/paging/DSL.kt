package com.droiddev.paging

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView

/**
 * Created with love by A.K.HTOO on 28/06/2020,June,2020.
 */

fun RecyclerView.attachPaging(helper: PagingHelper.() -> Unit): PagingHelper {
    val paging = PagingHelper().apply(helper)
    paging.attachToRecyclerView(this)
    return paging
}

val Fragment.viewLifecycle: () -> Lifecycle
    get() = { viewLifecycleOwner.lifecycle }