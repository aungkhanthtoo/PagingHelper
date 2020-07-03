package com.dev.droid.ui.networkonly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dev.droid.R
import com.dev.droid.data.network.livedatacalladapter.observe
import com.dev.droid.ui.networkroom.MoviePagingAdapter
import com.droiddev.paging.PagingHelper
import kotlinx.android.synthetic.main.main_fragment.*

class MoviesFragment : Fragment() {

    private val viewModel: MoviesViewModel by viewModels()

    private val pagingHelper = PagingHelper()
    private val pagingAdapter = MoviePagingAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(rvMovie) {
            setHasFixedSize(true)
            adapter = pagingAdapter
        }

        pagingHelper.onLoadMore = viewModel
        pagingHelper.threshold = 10
        pagingHelper.pageSize = 20
        pagingHelper.attachToRecyclerView(rvMovie)
        refresh.setOnRefreshListener(pagingHelper::refresh)

        viewModel.topRatedMovies.observe(viewLifecycleOwner, ::showError) { response ->
            pagingHelper.totalPages = response.totalPages
            pagingAdapter.setPaging(response.movies)
            stopIfRefreshing()
        }
    }

    private fun showError(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
        stopIfRefreshing()
        pagingHelper.fallback()
    }

    private fun stopIfRefreshing() {
        if (refresh.isRefreshing) {
            refresh.isRefreshing = false
        }
    }

    companion object {
        fun newInstance() = MoviesFragment()
    }
}