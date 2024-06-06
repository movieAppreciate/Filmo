package com.teamfilmo.filmo.ui.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.teamfilmo.filmo.databinding.FragmentBottomSheetBinding

class ModalBottomSheet : BottomSheetDialogFragment() {
    private var binding: FragmentBottomSheetBinding? = null

    var listener: OnButtonSelectedListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        val view = binding!!.root

        binding!!.tvFirst.setOnClickListener {
            listener?.onButtonSelected(binding!!.tvFirst.text.toString())
        }
        binding!!.tvSecond.setOnClickListener {
            listener?.onButtonSelected(binding!!.tvSecond.text.toString())
        }
        binding!!.tvThird.setOnClickListener {
            listener?.onButtonSelected(binding!!.tvThird.text.toString())
        }
        return view
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}

interface OnButtonSelectedListener {
    fun onButtonSelected(text: String)
}
