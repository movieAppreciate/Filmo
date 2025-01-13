package com.teamfilmo.filmo.ui.write.select

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentSelectMovieBinding
import com.teamfilmo.filmo.ui.write.adapter.MoviePosterAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MovieSelectFragment :
    BaseFragment<FragmentSelectMovieBinding, MovieSelectViewModel, MovieSelectEffect, MovieSelectEvent>(
        FragmentSelectMovieBinding::inflate,
    ) {
    private var queryText: String? = null
    private val moviePosterAdapter by lazy {
        context?.let { MoviePosterAdapter(it) }
    }
    private val navController by lazy { findNavController() }

    companion object {
        fun newInstance(): MovieSelectFragment {
            val args = Bundle()
            val fragment = MovieSelectFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun navigateWriteReportFragment(
        movieName: String,
        movieId: Int,
    ) {
        val action = MovieSelectFragmentDirections.navigateToWriteReport(movieName = movieName, movieId = movieId.toString())

        navController.navigate(action)
    }

    override val viewModel: MovieSelectViewModel by viewModels()

    override fun onBindLayout() {
        super.onBindLayout()

        val spanCount = 3
        val layoutManager = GridLayoutManager(requireContext(), spanCount)
        binding.movieRecyclerView.layoutManager = layoutManager
        binding.movieRecyclerView.adapter = moviePosterAdapter

        /*
        Fragment는 viewLifecycleOwner.lifecycleScope를 사용해야 한다.
        - lifecycleScope.launch
         - 코루틴을 프래그먼트의 수명주기와 연결한다.
         - 프래그먼트가 유효한 상태에 있는 한 코루틴이 계속 실행된다.
         - 프래그먼트의 뷰가 삭제되어도 계속 실행될 수 있다.
         - 프래그먼트가 뷰에 의존하지 않는 작업(데이터 처리 또는 백그라운드 작업)에 적합


        -viewLifecycleOwner.lifecycleScope.launch :
         - 코루틴을 프래그먼트 뷰의 수명주기에 연결한다.
         - 뷰가 파괴될 때 코루틴이 자동으로 취소된다.
         - 일반적으로 프래그먼트가 중지되거나 교체될 때 발생한다.
         - UI 관련 작업이나 UI를 업데이트하는 (StateFlow를 관찰할 때) 선호되며, 뷰가 유효한 상태일 때 업데이트가 이루어지도록 함.

         그럼 왜 액티비티에서는 그냥 lifecycleScope.launch을 사용해도 될까?
         - 액티비티는 별도의 뷰 수명주기가 없다.
         - 액티비티의 뷰는 액티비티 자체와 직접 연결되기 대문에 존재하지 않는 뷰를 업데이트하려는 시도 위험이 없다.
         */

        // PagingData 스트림을 관찰하고 생성된 각 값을 어댑터의 submitData() 메서드에 전달한다.
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.moviePosterPathFlow.collectLatest {
                    moviePosterAdapter?.submitData(it)
                }
            }
        }

        // moviePosterAdapter의 loadStateFlow 속성에서 값을 수집한다.
        // 데이터의 현재 로드상태(로드 중, 성공적으로 로드되었는지, 오류가 발생했는지)를 내보낸다.
        // collectLatest : 흐름에서 방출된 최신값을 수집한다.
        // 가장 최근 상태만 중요한 UI 업데이트에서 유용하다.
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                moviePosterAdapter?.loadStateFlow?.collectLatest {
                    // todo: 검색 결과가 없는 경우
                    val isListEmpty =
                        it.source.refresh is LoadState.NotLoading &&
                            moviePosterAdapter?.itemCount == 0

                    if (isListEmpty) {
                        Toast.makeText(context, "검색 결과가 없어요", Toast.LENGTH_SHORT).show()
                    }
                    binding.movieProgressBarAppend.isVisible = it.source.append is LoadState.Loading
                }
            }
        }

        // 여기서 뒤로 가기를 하면 allmoviereportfragment가 새로굄된다.
        binding.btnBack.setOnClickListener {
            navController.popBackStack()
            // navController.navigate(R.id.allMovieReportFragment)
        }

        binding.movieSearchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    // 처음 검색 시에는 1페이지 데이터를 가져옴
                    queryText = query
                    viewModel.handleEvent(MovieSelectEvent.SearchMovie(queryText))
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.handleEvent(MovieSelectEvent.InitializeMovieList)
                    return true
                }
            },
        )

        /*
        그냥 id와 name을 다 어댑터에서 넘겨주도록 함.
         */
        moviePosterAdapter?.setOnItemClickListener(
            object : MoviePosterAdapter.OnItemClickListener {
                override fun onItemClick(
                    movieId: Int,
                    movieName: String,
                    uri: String,
                ) {
                    Timber.d("movieName :$movieName")
                    navigateWriteReportFragment(movieName, movieId)
                }
            },
        )
    }
}
