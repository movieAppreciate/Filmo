package com.teamfilmo.filmo.ui.write.select

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentSelectMovieBinding
import com.teamfilmo.filmo.ui.write.WriteActivity
import com.teamfilmo.filmo.ui.write.adapter.MoviePosterAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MovieSelectFragment : BaseFragment<FragmentSelectMovieBinding, MovieSelectViewModel, MovieSelectEffect, MovieSelectEvent>(
    FragmentSelectMovieBinding::inflate,
) {
    private var queryText: String? = null
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

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movieFlow.collectLatest {
                    Timber.d("MoviePosterAdapter", "Submitting new data")
                    moviePosterAdapter?.submitData(it)
                    Timber.d("MoviePosterAdapter", "Data submitted")
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                moviePosterAdapter?.loadStateFlow?.collect {
                    Timber.d("refresh : $it.refresh")
                    binding.movieProgressBar.isVisible = it.refresh is LoadState.Loading
                }
            }
        }

        val spanCount = 3
        val layoutManager = GridLayoutManager(requireContext(), spanCount)
        binding.movieRecyclerView.layoutManager = layoutManager
        binding.btnBack.setOnClickListener {
            (activity as? WriteActivity)?.navigateToAllMovieReportFragment()
        }

        binding.movieRecyclerView.adapter = moviePosterAdapter
        binding.movieSearchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    // 처음 검색 시에는 1페이지 데이터를 가져옴
                    queryText = query
                    viewModel.handleEvent(MovieSelectEvent.SearchMovie(queryText))
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
//                    moviePosterAdapter?.initializePosterUriList()
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
                            viewModel.movieList.collect { movieList ->
                                Timber.d("클릭함 $movieList")
                                if (movieList.isNotEmpty()) {
                                    val selectedMovieName = movieList[position].title
                                    val selectedMovieId = movieList[position].id.toString()
                                    (activity as? WriteActivity)?.navigateToWriteReportFragment(selectedMovieName, selectedMovieId)
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
//                lifecycleScope.launch {
//                    repeatOnLifecycle(Lifecycle.State.STARTED) {
//                        viewModel.moviePosterUriList.collect {
//                            moviePosterAdapter?.setPosterUriList(it)
//                        }
//                    }
//                }
            }
        }
    }
}
