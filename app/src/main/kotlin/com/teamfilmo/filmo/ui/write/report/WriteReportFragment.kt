package com.teamfilmo.filmo.ui.write.report

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.data.remote.entity.report.regist.RegistReportRequest
import com.teamfilmo.filmo.databinding.FragmentWriteReportBinding
import com.teamfilmo.filmo.ui.main.MainActivity
import com.teamfilmo.filmo.ui.widget.CustomDialog
import com.teamfilmo.filmo.ui.widget.ItemClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WriteReportFragment :
    BaseFragment<FragmentWriteReportBinding, WriteReportViewModel, WriteReportEffect, WriteReportEvent>(
        FragmentWriteReportBinding::inflate,
    ) {
    override val viewModel: WriteReportViewModel by viewModels()
    private var tagString: String? = null
    private val navController by lazy { findNavController() }
    private var uri: String? = null
    val args: WriteReportFragmentArgs by navArgs()

    companion object {
        fun newInstance(): WriteReportFragment {
            val args = Bundle()
            val fragment = WriteReportFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onBindLayout() {
        fun EditText.hideKeyboard() {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, 0)
        }

        // todo : 화면 스크롤 시 감지하기

        binding.scrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // 스크롤 방향 감지
            when {
                scrollY > oldScrollY -> {
                    // 아래로 스크롤
                    hideKeyboard(v)
                }
            }
        }

        binding.editReportBody
            .setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO) {
                    hideKeyboard(binding.editReportBody)
                    binding.scrollView.scrollToDescendant(binding.ivThumbnail)
                    true
                } else {
                    false
                }
            }
        binding.editReportTag.apply {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO) {
                    hideKeyboard()
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
                        removeTextChangedListener(this)
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
                        addTextChangedListener(this)
                    }
                },
            )
        }

        // ReportThumbnailFragment에서 보낸 데이터 받기
        setFragmentResultListener("requestKey") { key, bundle ->
            uri = bundle.getString("uri")
            Glide
                .with(this@WriteReportFragment)
                .load(uri)
                .into(binding.ivThumbnail)
            binding.btnSelectPoster.text = "이미지 변경"
            binding.btnReportRegister.setTextColor(requireContext().getColor(R.color.primary))
        }

        binding.txtSelectedMovie.text = args.movieName
        binding.btnReportRegister.setOnClickListener {
            if (binding.editReportTitle.text != null && binding.editReportBody.text != null && uri != null) {
                val request =
                    RegistReportRequest(
                        title = binding.editReportTitle.text.toString(),
                        content =
                            binding.editReportBody.text
                                .toString(),
                        imageUrl = uri!!,
                        movieId = args.movieId,
                        tagString = tagString?.replace(" ", "").toString(),
                    )

                viewModel.handleEvent(WriteReportEvent.RegisterReport(request))
            } else {
                // 키보드 내리기

                if (binding.editReportTitle.text?.length == 0) {
                    Toast.makeText(context, "감상문 제목을 입력해주세요", Toast.LENGTH_SHORT).show()
                } else if (binding.editReportBody.text
                        ?.length == 0
                ) {
                    Toast.makeText(context, "감상문 내용을 입력해주세요", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "포스터를 선택해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnSelectPoster.setOnClickListener {
            val action = WriteReportFragmentDirections.navigateToThumbnail(movieName = args.movieName, movieId = args.movieId)
            navController.navigate(action)
        }

        binding.btnWriteReportBack.setOnClickListener {
            showModifyMovieDialog()
        }
    }

    // 영화 변경  다이얼로그
    private fun showModifyMovieDialog() {
        val dialog =
            context?.let {
                CustomDialog(
                    button2Text = "영화 변경",
                    dialogMessage = "감상문을 작성할 영화를 변경할까요?",
                )
            }
        dialog?.show(parentFragmentManager, "WriteReportFragment")
        dialog?.setButton2ClickListener(
            object : ItemClickListener {
                override fun onButton2Click() {
                    navController.popBackStack(R.id.movieSelectFragment, false)
                }

                override fun onButton1Click() {
                    dialog.dismiss()
                }
            },
        )
    }

    override fun handleEffect(effect: WriteReportEffect) {
        when (effect) {
            is WriteReportEffect.NavigateToMain -> {
                Toast.makeText(context, "감상문이 등록되었습니다", Toast.LENGTH_LONG).show()
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
