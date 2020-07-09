package com.dev.droid.ui.networkonly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.droid.R
import com.dev.droid.data.network.livedatacalladapter.observe
import com.dev.droid.ui.networkroom.MovieGridPagingAdapter
import com.dev.droid.ui.networkroom.MoviePagingAdapter
import com.droiddev.paging.PagingHelper
import kotlinx.android.synthetic.main.grid_fragment.*
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.main_fragment.refresh

class MovieGridFragment :Fragment(){
    private val viewModel: MoviesViewModel by viewModels()
    private val pagingHelper = PagingHelper()
    private val pagingAdapter = MovieGridPagingAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.grid_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(rvGridMovie) {
            setHasFixedSize(true)
            adapter = pagingAdapter
        }
        //rvGridMovie.layoutManager = GridLayoutManager(requireContext(),2)
        rvGridMovie.layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.HORIZONTAL,false)

        pagingHelper.onLoadMore = viewModel
        pagingHelper.threshold = 20
        pagingHelper.pageSize = 20
        pagingHelper.attachToRecyclerView(rvGridMovie)
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
        fun newInstance() = MovieGridFragment()
    }


}