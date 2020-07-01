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
import com.droiddev.paging.PagingHelper
import kotlinx.android.synthetic.main.main_fragment.*

class MoviesFragment : Fragment() {

    private val viewModel: MoviesViewModel by viewModels()
    private val pagingHelper = PagingHelper()
    private val pagingAdapter = MovieAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pagingHelper.threshold = 2
        pagingHelper.onLoadMore = viewModel
        with(rvMovie) {
            setHasFixedSize(true)
            adapter = pagingAdapter
            pagingHelper.attachToRecyclerView(this)
        }
        refresh.setOnRefreshListener(pagingHelper::refresh)

        viewModel.topRatedMovies.observe(viewLifecycleOwner, ::showError) { response ->
            pagingHelper.totalPages = response.totalPages
            rvMovie.postDelayed({
                pagingAdapter.setPaging(response.movies)
            }, 900)

            stopRefreshing()
        }
    }

    private fun showError(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
        stopRefreshing()
    }

    private fun stopRefreshing() {
        if (refresh.isRefreshing) {
            refresh.isRefreshing = false
        }
    }

    companion object {
        fun newInstance() = MoviesFragment()
    }
}