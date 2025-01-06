package com.teamfilmo.filmo.ui.modify

import android.os.Bundle
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
import com.teamfilmo.filmo.data.remote.entity.report.update.UpdateReportRequest
import com.teamfilmo.filmo.databinding.FragmentModifyReportBinding
import com.teamfilmo.filmo.ui.widget.CustomDialog
import com.teamfilmo.filmo.ui.widget.ItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ModifyReportFragment :
    BaseFragment<FragmentModifyReportBinding, ModifyReportViewModel, ModifyReportEffect, ModifyReportEvent>(
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
        Glide
            .with(binding.root.context)
            .asBitmap()
            .load(imageUrl)
            .into(view as ImageView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
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
        return super.onCreate(savedInstanceState)
    }

    // 감상문 수정 이탈 다이얼로그
    private fun showCancelModifyReportDialog() {
        val button1Text = "종료"
        val dialog =
            context?.let {
                val dialogMessage = "수정하기를 종료하시겠어요?"
                val dialogSubMessage = "변경된 내용은 저장되지 않아요"
                val button2Text = "계속 작성"
                CustomDialog(
                    dialogMessage = dialogMessage,
                    dialogSubMessage = dialogSubMessage,
                    button1Text = button1Text,
                    button2Text = button2Text,
                )
            }

        dialog?.setItemClickListener(
            object : ItemClickListener {
                override fun onClick() {
                    dialog.dismiss()
                }
            },
        )
    }

    override fun onBindLayout() {
        lifecycleScope.launch {
            viewModel.getReport(args.reportId)
        }
        // 받은 args
        Timber.d("args : $args")
        // 만약 썸네일을 변경한다면 ? ReportThumbnailFragment에서 보낸 데이터 받기
        // todo : 변경하지 않는다면?
        setFragmentResultListener("requestKey") { key, bundle ->
            uri = bundle.getString("uri")
            getImage(uri.toString(), binding.ivThumbnail)
            binding.btnSelectPoster.text = "이미지 변경"
        }

        lifecycleScope.launch {
            viewModel.getReportResponse.collect {
                uri = it.imageUrl
            }
        }

        with(binding) {
            btnSelectPoster.setOnClickListener {
                // 포스터 선택하기
                val action =
                    ModifyReportFragmentDirections.navigateToReportThumbnail(
                        movieId =
                            viewModel.getReportResponse.value.movieId
                                .toString(),
                        movieName = args.movieName,
                    )
                navController.navigate(action)
            }

            btnReportModify.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.updateReport(
                        UpdateReportRequest(
                            reportId = args.reportId,
                            title = editReportTitle.text.toString(),
                            content = editReportBody.text.toString(),
                            // todo : 이미지를  변경한 경우 uri로 변경해주기
                            imageUrl = uri.toString(),
                            tagString = tagString?.replace(" ", "").toString(),
                            movieId = args.movieId,
                        ),
                    )
                    // 바디로 다시 이동하기
                    val action = ModifyReportFragmentDirections.navigateToBody(args.reportId)
                    navController.navigate(action)
                }
                // 감상문 수정하기
            }

            binding.txtSelectedMovie.text = args.movieName

            btnBack.setOnClickListener {
                // todo : 다이얼로그 띄우기
                showCancelModifyReportDialog()
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
        }
    }
}
