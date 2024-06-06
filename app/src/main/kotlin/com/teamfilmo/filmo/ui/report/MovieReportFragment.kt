package com.teamfilmo.filmo.ui.report

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentMovieReportBinding
import com.teamfilmo.filmo.ui.report.recyclerview.ReportAdapter
import com.teamfilmo.filmo.ui.report.recyclerview.ReportAdapter2
import com.teamfilmo.filmo.ui.report.recyclerview.ReportPayload
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieReportFragment : BaseFragment<FragmentMovieReportBinding>(FragmentMovieReportBinding::inflate), View.OnClickListener {
    private val reportViewModel by activityViewModels<ReportViewModel>()
    val reportAdapter by lazy {
        ReportAdapter()
    }
    private val reportAdapter2 by lazy {
        ReportAdapter2()
    }

    private fun onRefresh() {
        binding.swiperefresh.setOnRefreshListener {
            lifecycleScope.launch {
                reportViewModel.requestReport()
                reportViewModel.report.value?.let {
                    binding.reportRecyclerview1.apply {
                        visibility = View.VISIBLE
                        adapter = reportAdapter
                        reportAdapter.setReportInfo(it, 0, 2)
                    }
                    binding.reportRecyclerview2.apply {
                        visibility = View.VISIBLE
                        adapter = reportAdapter2
                        reportAdapter2.setReportInfo(it, 3, it.lastIndex)
                    }
                }
                reportViewModel.bookmarkList.value?.let {
                    reportAdapter.setBookmark(it)
                    reportAdapter2.setBookmark(it)
                }
                binding.swiperefresh.isRefreshing = false
            }
        }

        /*
        장르 버튼 구현
         */
    }

    override fun initViews() {
        super.initViews()

        childFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.recommend_fragment_container_view, RecommendReportFragment())
        }
        reportViewModel.bookmarkList.observe(viewLifecycleOwner) { bookmarkList ->
            binding.reportRecyclerview1.apply {
                adapter = reportAdapter
                reportAdapter.setBookmark(bookmarkList)
            }

            binding.reportRecyclerview2.apply {
                adapter = reportAdapter2
                reportAdapter2.setBookmark(bookmarkList)
            }
        }

        reportViewModel.report.observe(viewLifecycleOwner) { reportList ->
            binding.reportRecyclerview1.apply {
                adapter = reportAdapter
                reportAdapter.setReportInfo(reportList, 0, 2)
            }

            binding.reportRecyclerview2.apply {
                adapter = reportAdapter2
                reportAdapter2.setReportInfo(reportList, 3, reportList.lastIndex)
            }
        }
        reportAdapter.itemClick =
            object : ReportAdapter.ItemClick {
                override fun onClick(position: Int) {
                    Toast.makeText(context, "감상문 클릭", Toast.LENGTH_SHORT).show()
                    // todo : 본문 페이지로 이동하기
                }

                override fun onLikeClick(position: Int) {
                    val report = reportAdapter.reportList[position]
                    lifecycleScope.launch {
                        reportViewModel.updateLike(report.reportId)
                    }
                }

                override fun onBookmarkClick(position: Int) {
                    val report = reportAdapter.reportList[position]
                    lifecycleScope.launch {
                        reportViewModel.toggleBookmark(report.reportId)
                    }
                }
            }
    }

    override fun initObserver() {
        super.initObserver()

        reportViewModel.uiState.observe(viewLifecycleOwner) { uistate ->
            Log.d("ui state 변경", uistate.toString())
        }

        reportViewModel.likeState.observe(viewLifecycleOwner) { likeState ->
            Log.d("좋아요 변경", likeState.toString())

            val index = reportAdapter.reportList.indexOfFirst { it.reportId == likeState.reportId }
            val index2 = reportAdapter2.reportList.indexOfFirst { it.reportId == likeState.reportId }
            if (index != -1) {
                reportAdapter.notifyItemChanged(index, ReportPayload.LikePayload(likeState.isLiked))
                reportAdapter.notifyItemChanged(index, ReportPayload.LikeCountPayload(likeState.likeCount))
            } else {
                reportAdapter2.notifyItemChanged(index2, ReportPayload.LikePayload(likeState.isLiked))
                reportAdapter2.notifyItemChanged(index2, ReportPayload.LikeCountPayload(likeState.likeCount))
            }
        }

        reportViewModel.bookmarkState.observe(viewLifecycleOwner) { bookmarkState ->
            Log.d("북마크 변경", bookmarkState.isBookmarked.toString())
            val index = reportAdapter.bookmarkList.indexOfFirst { it.reportId == bookmarkState.reportId }
            val index2 = reportAdapter2.bookmarkList.indexOfFirst { it.reportId == bookmarkState.reportId }

            if (index != -1) {
                reportAdapter.notifyItemChanged(index, ReportPayload.BookmarkPayload(bookmarkState.isBookmarked))
            } else {
                reportAdapter2.notifyItemChanged(index2, ReportPayload.BookmarkPayload(bookmarkState.isBookmarked))
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
                reportViewModel.filterGenreReport(genre)
                Log.d("장르 버튼 클릭", reportViewModel.filteredReportList.value.toString())
                val filteredList = reportViewModel.filteredReportList.value

                if (!filteredList.isNullOrEmpty()) {
                    if (filteredList.size <= 1) {
                        binding.reportRecyclerview1.visibility = View.VISIBLE
                        binding.reportRecyclerview2.visibility = View.GONE

                        reportAdapter.setReportInfo(filteredList, 0, 0)
                    }
                    if (filteredList.size <= 3) {
                        binding.reportRecyclerview1.visibility = View.VISIBLE
                        binding.reportRecyclerview2.visibility = View.GONE

                        reportAdapter.setReportInfo(filteredList, 0, filteredList.lastIndex)
                    } else {
                        binding.reportRecyclerview1.visibility = View.VISIBLE
                        binding.reportRecyclerview2.visibility = View.VISIBLE
                        reportAdapter.setReportInfo(filteredList, 0, 2)
                        reportAdapter2.setReportInfo(filteredList, 3, filteredList.lastIndex)
                    }
                } else {
                    binding.reportRecyclerview1.visibility = View.GONE
                    binding.reportRecyclerview2.visibility = View.GONE
                }
            }
        } else {
            onRefresh()
        }
    }
}
