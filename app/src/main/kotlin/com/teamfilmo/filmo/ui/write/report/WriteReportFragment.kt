package com.teamfilmo.filmo.ui.write.report

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.data.remote.model.report.RegistReportRequest
import com.teamfilmo.filmo.databinding.FragmentWriteReportBinding
import com.teamfilmo.filmo.ui.write.WriteActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WriteReportFragment : BaseFragment<FragmentWriteReportBinding, WriteReportViewModel, WriteReportEffect, WriteReportEvent>(
    FragmentWriteReportBinding::inflate,
) {
    override val viewModel: WriteReportViewModel by viewModels()
    private var thumbnailUri: String? = null
    private var tagString: String? = null

    companion object {
        fun newInstance(
            movieName: String,
            movieId: String,
        ): WriteReportFragment {
            return WriteReportFragment().apply {
                arguments =
                    Bundle().apply {
                        putString("MOVIE_NAME", movieName)
                        putString("MOVIE_ID", movieId)
                    }
            }
        }
    }

    override fun onBindLayout() {
        fun EditText.hideKeyboard() {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, 0)
        }

        binding.editReportTag.apply {
            val inputType = InputType.TYPE_CLASS_TEXT

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
            parentFragmentManager.setFragmentResultListener("REQUEST_OK", this@WriteReportFragment) { key, bundle ->
                val selectedThumbnailUri = bundle.getString("SELECTED_IMAGE_URI")
                if (selectedThumbnailUri != null) {
                    thumbnailUri = selectedThumbnailUri
                    Glide.with(this@WriteReportFragment)
                        .load(selectedThumbnailUri)
                        .into(binding.ivThumbnail)
                    binding.btnSelectPoster.text = "이미지 변경"
                }
            }
        }

        val movieName = arguments?.getString("MOVIE_NAME")
        val movieId = arguments?.getString("MOVIE_ID")
        binding.txtSelectedMovie.text = movieName

        val list = arrayListOf<String>()
        val title = binding.editReportTitle.text
        val content = binding.editReportBody.text
        val tag = binding.editReportTag.text.toString()

        list.add(tag)
        binding.btnReportRegister.setOnClickListener {
            if (movieId != null && title != null && content != null && thumbnailUri != null) {
                val request =
                    RegistReportRequest(
                        title = title.toString(),
                        content = content.toString(),
                        imageUrl = thumbnailUri.toString(),
                        movieId = movieId.toString(),
                        tagString = tagString.toString(),
                    )
                viewModel.handleEvent(WriteReportEvent.RegisterReport("tjdgustjdan@gmail.com", request))
            }
        }

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.addCallback(
                this,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (parentFragmentManager.backStackEntryCount > 1) {
                            childFragmentManager.popBackStack()
                        } else {
                            isEnabled = false
                            requireActivity().onBackPressed()
                        }
                    }
                },
            )
        }

        binding.btnSelectPoster.setOnClickListener {
            // 포스터 선택 프래그먼트 열기
            if (movieName != null && movieId != null) {
                (activity as? WriteActivity)?.navigateToReportThumbnailFragment(movieName, movieId)
            }
        }

        binding.btnBack.setOnClickListener {
            showConfirmationDialog()
        }
    }

    private fun handleBackNavigation() {
        if (parentFragmentManager.backStackEntryCount > 0) {
            parentFragmentManager.popBackStack()
        } else {
            requireActivity().onBackPressed()
        }
    }

    private fun showConfirmationDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("영화 변경")
        dialogBuilder.setMessage("감상문을 작성할 영화를 변경하시겠습니까?")
        dialogBuilder.setPositiveButton("네!") { _, _ ->
            handleBackNavigation()
        }
        dialogBuilder.setNegativeButton("아니요!") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    override fun handleEffect(effect: WriteReportEffect) {
        when (effect) {
            is WriteReportEffect.NavigateToMain -> {
                Toast.makeText(context, "감상문이 등록되었습니다", Toast.LENGTH_LONG).show()
                (activity as? WriteActivity)?.navigateToAllMovieReportFragment()
            }
        }
    }
}
