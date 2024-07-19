package com.teamfilmo.filmo.ui.report.all

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentAllMovieReportBinding
import com.teamfilmo.filmo.ui.report.adapter.AllMovieReportAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class AllMovieReportFragment :
    BaseFragment<FragmentAllMovieReportBinding, AllMovieReportViewModel, AllMovieReportEffect, AllMovieReportEvent>(
        FragmentAllMovieReportBinding::inflate,
    ),
    View.OnClickListener {
    override val viewModel: AllMovieReportViewModel by viewModels()

    val allMovieReportAdapter by lazy {
        AllMovieReportAdapter()
    }

    private fun onRefresh() {
        binding.swiperefresh.setOnRefreshListener {
            lifecycleScope.launch {
                binding.swiperefresh.isRefreshing = false
            }
        }
    }

    override fun handleEffect(effect: AllMovieReportEffect) {
        when (effect) {
            is AllMovieReportEffect.RegistLike -> {
                // 좋아요 아이콘 변경
                allMovieReportAdapter.updateLikeState(effect.reportId, true)
            }
            is AllMovieReportEffect.CancelLike -> {
                allMovieReportAdapter.updateLikeState(effect.reportId, false)
            }
        }
    }

    override fun onBindLayout() {
        childFragmentManager.commit {
            setReorderingAllowed(true)

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.list.collect {
                        binding.allMovieReportRecyclerview.apply {
                            adapter = allMovieReportAdapter
                            allMovieReportAdapter.setReportInfo(viewModel.list.value)
                        }
                    }
                }
            }
        }

        allMovieReportAdapter.itemClick =
            object : AllMovieReportAdapter.ItemClick {
                override fun onClick(position: Int) {
                    Toast.makeText(context, "감상문 클릭", Toast.LENGTH_SHORT).show()
                    // todo  : 본문 페이지로 이동
                }

                override fun onLikeClick(position: Int) {
                    Timber.d(TAG, "clicked")
                    val report = allMovieReportAdapter.reportList[position]
                    lifecycleScope.launch {
                        val result = viewModel.checkLike(report.reportId)
                        result.collect {
                            if (it) {
                                viewModel.handleEvent(AllMovieReportEvent.CancelLike(report.reportId))
                            } else {
                                viewModel.handleEvent(AllMovieReportEvent.RegistLike(report.reportId))
                            }
                        }
                    }
                }

                override fun onBookmarkClick(position: Int) {
                    val report = allMovieReportAdapter.reportList[position]
                    lifecycleScope.launch {
//                        reportViewModel.toggleBookmark(report.reportId)
                    }
                }
            }

        binding.sf.setOnClickListener {
            onClick(binding.sf)
        }

        binding.romance.setOnClickListener {
            onClick(binding.romance)
        }

        binding.tvMovie.setOnClickListener {
            onClick(binding.tvMovie)
        }

        binding.family.setOnClickListener {
            onClick(binding.family)
        }

        binding.horror.setOnClickListener {
            onClick(binding.horror)
        }

        binding.documentary.setOnClickListener {
            onClick(binding.documentary)
        }

        binding.drama.setOnClickListener {
            onClick(binding.drama)
        }

        binding.romance.setOnClickListener {
            onClick(binding.romance)
        }

        binding.adventure.setOnClickListener {
            onClick(binding.adventure)
        }

        binding.mystery.setOnClickListener {
            onClick(binding.mystery)
        }

        binding.crime.setOnClickListener {
            onClick(binding.crime)
        }
        binding.western.setOnClickListener {
            onClick(binding.western)
        }

        binding.thriller.setOnClickListener {
            onClick(binding.thriller)
        }
        binding.animation.setOnClickListener {
            onClick(binding.animation)
        }

        binding.action.setOnClickListener {
            onClick(binding.action)
        }

        binding.history.setOnClickListener {
            onClick(binding.history)
        }

        binding.music.setOnClickListener {
            onClick(binding.music)
        }
        binding.war.setOnClickListener {
            onClick(binding.war)
        }

        binding.comedy.setOnClickListener {
            onClick(binding.comedy)
        }

        binding.fantasy.setOnClickListener {
            onClick(binding.fantasy)
        }

        onRefresh()
    }

//    override fun initObserver() {
//        super.initObserver()
//
//        reportViewModel.uiState.observe(viewLifecycleOwner) { uistate ->
//            Log.d("ui state 변경", uistate.toString())
//        }
//
//        reportViewModel.likeState.observe(viewLifecycleOwner) { likeState ->
//            Log.d("좋아요 변경", likeState.toString())
//
//            val index = reportAdapter.reportList.indexOfFirst { it.reportId == likeState.reportId }
//            val index2 = reportAdapter2.reportList.indexOfFirst { it.reportId == likeState.reportId }
//            if (index != -1) {
//                reportAdapter.notifyItemChanged(index, ReportPayload.LikePayload(likeState.isLiked))
//                reportAdapter.notifyItemChanged(index, ReportPayload.LikeCountPayload(likeState.likeCount))
//            } else {
//                reportAdapter2.notifyItemChanged(index2, ReportPayload.LikePayload(likeState.isLiked))
//                reportAdapter2.notifyItemChanged(index2, ReportPayload.LikeCountPayload(likeState.likeCount))
//            }
//        }
//
//        reportViewModel.bookmarkState.observe(viewLifecycleOwner) { bookmarkState ->
//            Log.d("북마크 변경", bookmarkState.isBookmarked.toString())
//            val index = reportAdapter.bookmarkList.indexOfFirst { it.reportId == bookmarkState.reportId }
//            val index2 = reportAdapter2.bookmarkList.indexOfFirst { it.reportId == bookmarkState.reportId }
//
//            if (index != -1) {
//                reportAdapter.notifyItemChanged(index, ReportPayload.BookmarkPayload(bookmarkState.isBookmarked))
//            } else {
//                reportAdapter2.notifyItemChanged(index2, ReportPayload.BookmarkPayload(bookmarkState.isBookmarked))
//            }
//        }
//    }

    private fun deselectButton(selectedButton: Button) {
        val genreButton: List<Button> =
            listOf(
                binding.sf,
                binding.tvMovie,
                binding.family,
                binding.horror,
                binding.documentary,
                binding.drama,
                binding.romance,
                binding.adventure,
                binding.mystery,
                binding.crime,
                binding.western,
                binding.thriller,
                binding.animation,
                binding.action,
                binding.history,
                binding.music,
                binding.war,
                binding.comedy,
                binding.fantasy,
            )

        genreButton.forEach { button ->
            if (button != selectedButton) {
                button.isSelected = false
            }
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            v.isSelected = !(v as Button).isSelected
        }
        val genre = (v as Button).text.toString()

        deselectButton(v)

        if (v.isSelected) {
            lifecycleScope.launch {
//                reportViewModel.filterGenreReport(genre)
//                Log.d("장르 버튼 클릭", reportViewModel.filteredReportList.value.toString())
//                val filteredList = reportViewModel.filteredReportList.value
//
//                if (!filteredList.isNullOrEmpty()) {
//                    if (filteredList.size <= 1) {
//                        binding.reportRecyclerview1.visibility = View.VISIBLE
//                        binding.reportRecyclerview2.visibility = View.GONE
//
//                        reportAdapter.setReportInfo(filteredList, 0, 0)
//                    }
//                    if (filteredList.size <= 3) {
//                        binding.reportRecyclerview1.visibility = View.VISIBLE
//                        binding.reportRecyclerview2.visibility = View.GONE
//
//                        reportAdapter.setReportInfo(filteredList, 0, filteredList.lastIndex)
//                    } else {
//                        binding.reportRecyclerview1.visibility = View.VISIBLE
//                        binding.reportRecyclerview2.visibility = View.VISIBLE
//                        reportAdapter.setReportInfo(filteredList, 0, 2)
//                        reportAdapter2.setReportInfo(filteredList, 3, filteredList.lastIndex)
//                    }
//                } else {
//                    binding.reportRecyclerview1.visibility = View.GONE
//                    binding.reportRecyclerview2.visibility = View.GONE
//                }
            }
        } else {
            onRefresh()
        }
    }

    companion object {
        fun newInstance(): AllMovieReportFragment {
            val args = Bundle()

            val fragment = AllMovieReportFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
