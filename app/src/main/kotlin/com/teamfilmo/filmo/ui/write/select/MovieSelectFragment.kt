package com.teamfilmo.filmo.ui.write.select

import android.os.Bundle
import android.widget.AbsListView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentSelectMovieBinding
import com.teamfilmo.filmo.ui.write.WriteActivity
import com.teamfilmo.filmo.ui.write.adapter.MoviePosterAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MovieSelectFragment : BaseFragment<FragmentSelectMovieBinding, MovieSelectViewModel, MovieSelectEffect, MovieSelectEvent>(
    FragmentSelectMovieBinding::inflate,
) {
    private var queryText: String? = null
    private var currentPage: Int = 1
    private val moviePosterAdapter by lazy {
        context?.let { MoviePosterAdapter(it) }
    }

    companion object {
        fun newInstance(): MovieSelectFragment {
            val args = Bundle()

            val fragment = MovieSelectFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val viewModel: MovieSelectViewModel by viewModels()

    override fun onBindLayout() {
        super.onBindLayout()

//        binding.btnGetMovie.setOnClickListener {
//            currentPage++
//            viewModel.handleEvent(MovieSelectEvent.LoadNextPageMovie(queryText, currentPage))
//        }

        binding.movieGridView.setOnScrollListener(
            object : AbsListView.OnScrollListener {
                private var isLoading = false

                override fun onScrollStateChanged(
                    view: AbsListView?,
                    scrollState: Int,
                ) {
                }

                override fun onScroll(
                    view: AbsListView?,
                    firstVisibleItem: Int,
                    visibleItemCount: Int,
                    totalItemCount: Int,
                ) {
                    if (!binding.movieGridView.canScrollVertically(1) && !isLoading) {
                        isLoading = true
                        currentPage++
                        viewModel.handleEvent(MovieSelectEvent.LoadNextPageMovie(queryText, currentPage))
                    } else {
                        isLoading = false
                    }
                }
            },
        )
        binding.btnBack.setOnClickListener {
            (activity as? WriteActivity)?.navigateToAllMovieReportFragment()
        }

        binding.movieGridView.adapter = moviePosterAdapter
        binding.movieSearchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    // 처음 검색 시에는 1페이지 데이터를 가져옴
                    queryText = query
                    currentPage = 1
                    viewModel.handleEvent(MovieSelectEvent.SearchMovie(queryText, currentPage))
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    moviePosterAdapter?.initializePosterUriList()
                    viewModel.handleEvent(MovieSelectEvent.InitializeMovieList)
                    return true
                }
            },
        )

        moviePosterAdapter?.setOnItemClickListener(
            object : MoviePosterAdapter.OnItemClickListener {
                override fun onItemClick(
                    position: Int,
                    uri: String?,
                ) {
                    lifecycleScope.launch {
                        repeatOnLifecycle(Lifecycle.State.STARTED) {
                            moviePosterAdapter?.getSelectedMoviePosition().apply {
                                viewModel.movieList.collect { movieList ->
                                    this?.let {
                                        Timber.d("클릭함 $movieList")
                                        if (movieList.isNotEmpty()) {
                                            val selectedMovieName = movieList[this].title
                                            val selectedMovieId = movieList[this].id.toString()
                                            (activity as? WriteActivity)?.navigateToWriteReportFragment(selectedMovieName, selectedMovieId)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
        )
    }

    override fun handleEffect(effect: MovieSelectEffect) {
        when (effect) {
            is MovieSelectEffect.SearchMovie -> {
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.moviePosterUriList.collect {
                            moviePosterAdapter?.setPosterUriList(it)
//                            binding.btnGetMovie.visibility = View.VISIBLE
                        }
                    }
                }
            }

            is MovieSelectEffect.LoadNextPage -> {
                Timber.d("4. 프래그먼트의 LoadNextPage effect 호출 ")
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.moviePosterUriList.collect {
                            moviePosterAdapter?.initializePosterUriList()
                            moviePosterAdapter?.setPosterUriList(it)
                        }
                    }
                }
            }
        }
    }
}
