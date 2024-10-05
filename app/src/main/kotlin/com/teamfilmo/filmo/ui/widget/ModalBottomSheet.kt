package com.teamfilmo.filmo.ui.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.protobuf.option
import com.teamfilmo.filmo.databinding.FragmentBottomSheetBinding

class ModalBottomSheet : BottomSheetDialogFragment() {
    private var binding: FragmentBottomSheetBinding? = null
    private var listener: OnButtonSelectedListener? = null
    private var options: List<String> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        val view = binding!!.root
        return view
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setUpButton()
    }

    private fun setUpButton() {
        binding?.apply {
            tvFirst.apply {
                val option = options.getOrNull(0)
                visibility = if (option != null) View.VISIBLE else View.GONE
                text = option
                setOnClickListener {
                    // 텍스트 색 primary
                    option?.let {
                        listener?.onButtonSelected(it)
                    }
                }
            }
            tvSecond.apply {
                val option = options.getOrNull(1)
                visibility = if (option != null) View.VISIBLE else View.GONE
                text = option
                setOnClickListener { option?.let { listener?.onButtonSelected(it) } }
            }
            tvThird.apply {
                val option = options.getOrNull(2)
                visibility = if (option != null) View.VISIBLE else View.GONE
                text = option
                setOnClickListener { option?.let { listener?.onButtonSelected(it) } }
            }
        }
    }

    fun setListener(listener: OnButtonSelectedListener) {
        this.listener = listener
    }

    fun setOptions(newOptions: List<String>) {
        options = newOptions
        if (view != null) {
            setUpButton()
        }
    }

    companion object {
        const val TAG = "ModalBottomSheet"

        fun newInstance(options: List<String> = emptyList()): ModalBottomSheet {
            return ModalBottomSheet().apply {
                this.options = options
            }
        }
    }
}

interface OnButtonSelectedListener {
    fun onButtonSelected(text: String)
}
