package com.teamfilmo.filmo.ui.modify

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.data.remote.model.report.update.UpdateReportRequest
import com.teamfilmo.filmo.databinding.FragmentModifyReportBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ModifyReportFragment : BaseFragment<FragmentModifyReportBinding, ModifyReportViewModel, ModifyReportEffect, ModifyReportEvent>(
    FragmentModifyReportBinding::inflate,
) {
    override val viewModel: ModifyReportViewModel by viewModels()
    val args: ModifyReportFragmentArgs by navArgs()
    private val navController by lazy { findNavController() }
    private var tagString: String? = null

    private var uri: String? = null

    private fun getImage(
        imageUrl: String,
        view: View,
    ) {
        Glide.with(binding.root.context)
            .asBitmap()
            .load(imageUrl)
            .into(view as ImageView)
    }

    override fun onBindLayout() {
        // ReportThumbnailFragment에서 보낸 데이터 받기
        setFragmentResultListener("requestKey") { key, bundle ->
            uri = bundle.getString("uri")
            Glide.with(this@ModifyReportFragment)
                .load(uri)
                .into(binding.ivThumbnail)
            binding.btnSelectPoster.text = "이미지 변경"
        }

        with(binding) {
            btnSelectPoster.setOnClickListener {
                // 포스터 선택하기
                val action = ModifyReportFragmentDirections.navigateToReportThumbnail(movieId = viewModel.getReportResponse.value.movieId.toString(), movieName = args.movieName)
                navController.navigate(action)
            }

            btnReportModify.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.updateReport(
                        UpdateReportRequest(
                            reportId = viewModel.getReportResponse.value.reportId,
                            title = editReportTitle.text.toString(),
                            content = editReportBody.text.toString(),
                            // todo : 이미지를  변경한 경우 변경해주기
                            imageUri = viewModel.getReportResponse.value.imageUrl.toString(),
                            tagString = tagString?.replace(" ", "").toString(),
                            movieId = args.movieId.toString(),
                        ),
                    )
                }
                // 감상문 수정하기
            }

            btnBack.setOnClickListener {
                navController.popBackStack()
            }

            editReportTag.apply {
                setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO) {
                        true
                    } else {
                        false
                    }
                }

                addTextChangedListener(
                    object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int,
                        ) {
                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int,
                        ) {
                        }

                        override fun afterTextChanged(editable: Editable?) {
                            binding.editReportTag.removeTextChangedListener(this)
                            val inputText = editable.toString().trim()
                            val words = inputText.split(" ").filter { it.isNotEmpty() }
                            val formattedText =
                                words.joinToString(" ") { word ->
                                    if (word.startsWith("#")) word else "#$word"
                                }
                            tagString = formattedText
                            if (inputText != formattedText) {
                                binding.editReportTag.setText(formattedText)
                                binding.editReportTag.setSelection(formattedText.length)
                            }
                            binding.editReportTag.addTextChangedListener(this)
                        }
                    },
                )
            }
            lifecycleScope.launch {
                viewModel.getReport(args.reportId)
            }

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.getReportResponse.collect {
                        binding.editReportTitle.setText(it.title)
                        binding.editReportBody.setText(it.content)
                        binding.editReportTag.setText(it.tagString)

                        getImage("https://image.tmdb.org/t/p/original" + it.imageUrl, binding.ivThumbnail)
                    }
                }
            }

            binding.txtSelectedMovie.text = args.movieName
        }
    }
}
