package com.teamfilmo.filmo.ui.write.select

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
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

        val spanCount = 3
        val layoutManager = GridLayoutManager(requireContext(), spanCount)
        layoutManager.spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (moviePosterAdapter?.getItemViewType(position)) {
                        MoviePosterAdapter.VIEW_TYPE_LOADING -> spanCount
                        else -> 1
                    }
                }
            }
        binding.movieRecyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(
                    recyclerView: RecyclerView,
                    dx: Int,
                    dy: Int,
                ) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastVisibleItemPosition = (recyclerView.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
                    val itemTotalCount = recyclerView.adapter?.itemCount?.minus(1)

                    // 스크롤이 끝에 도달했는지 확인
                    if (lastVisibleItemPosition == itemTotalCount) {
                        currentPage++
                        binding.movieRecyclerView.post {
                            moviePosterAdapter?.setLoading(true)
                        }
                        viewModel.handleEvent(MovieSelectEvent.LoadNextPageMovie(queryText, currentPage))
                    }
                }
            },
        )
        binding.btnBack.setOnClickListener {
            (activity as? WriteActivity)?.navigateToAllMovieReportFragment()
        }

        binding.movieRecyclerView.adapter = moviePosterAdapter
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
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.moviePosterUriList.collect {
                            moviePosterAdapter?.setPosterUriList(it)
                        }
                    }
                }
            }

            is MovieSelectEffect.LoadNextPage -> {
                Timber.d("4. 프래그먼트의 LoadNextPage effect 호출 ")
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.moviePosterUriList.collect {
                            binding.movieRecyclerView.post {
                                moviePosterAdapter?.initializePosterUriList()
                                moviePosterAdapter?.setLoading(false)
                                moviePosterAdapter?.setPosterUriList(it)
                            }
                        }
                    }
                }
            }
        }
    }
}
