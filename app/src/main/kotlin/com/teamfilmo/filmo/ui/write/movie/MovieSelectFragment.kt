package com.teamfilmo.filmo.ui.write.movie

import android.os.Bundle
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieSelectFragment : BaseFragment<FragmentSelectMovieBinding, MovieSelectViewModel, MovieSelectEffect, MovieSelectEvent>(
    FragmentSelectMovieBinding::inflate,
) {
    private var currentPage: Int = 2
    private var myQuery: String? = null
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

//        binding.movieGridView.setOnScrollListener(
//            object : AbsListView.OnScrollListener {
//                private var isLoading = false
//
//                override fun onScrollStateChanged(
//                    view: AbsListView?,
//                    scrollState: Int,
//                ) {
//                    Timber.d("scroll state : $scrollState")
//                }
//
//                override fun onScroll(
//                    view: AbsListView?,
//                    firstVisibleItem: Int,
//                    visibleItemCount: Int,
//                    totalItemCount: Int,
//                ) {
//                    if (firstVisibleItem + visibleItemCount >= totalItemCount && totalItemCount > 0) {
//                        Timber.d("스크롤이 끝에 도달했습니다.")
//                        isLoading = true
//
//                        lifecycleScope.launch {
//                            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                                viewModel.totalPage.collect {
//                                    Timber.d("total 페이지 : $it")
//
//                                    Timber.d("호출 되는 페이지 : $currentPage")
//                                    //   viewModel.handleEvent(MovieSelectEvent.SearchMovie(myQuery, currentPage))
//                                }
//                            }
//                        }
//                    } else {
//                        isLoading = false
//                    }
//                }
//            },
//        )
        binding.btnBack.setOnClickListener {
            (activity as? WriteActivity)?.navigateToAllMovieReportFragment()
        }

        binding.movieGridView.adapter = moviePosterAdapter
        binding.movieSearchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    myQuery = query
                    // 처음 검색 시에는 1페이지 데이터를 가져옴
                    viewModel.handleEvent(MovieSelectEvent.SearchMovie(myQuery))
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
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
                                        if (movieList.isNotEmpty()) {
                                            val selectedMovieName = movieList[this].title
                                            val selectedMovieId = movieList[this].id
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
                        }
                    }
                }
            }
        }
    }
}
