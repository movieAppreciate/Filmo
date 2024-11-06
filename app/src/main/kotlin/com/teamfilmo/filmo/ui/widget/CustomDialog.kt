package com.teamfilmo.filmo.ui.widget

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.teamfilmo.filmo.databinding.CustomDialogBinding

interface ItemClickListener {
    fun onClick()
}

class CustomDialog(
    private val button1Text: String = "취소",
    private val button2Text: String,
    private val dialogSubMessage: String? = null,
    dialogMessage: String,
) : DialogFragment() {
    private lateinit var itemClickListener: ItemClickListener
    private lateinit var binding: CustomDialogBinding
    private val message = dialogMessage

    fun setItemClickListener(listener: ItemClickListener) {
        this.itemClickListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreate(savedInstanceState)

        binding = CustomDialogBinding.inflate(LayoutInflater.from(context))
        val view = binding.root
        // 다이얼로그를 둥글게 표현하기 위해 필요
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        // 다이얼로그 바깥쪽 클릭 시 종료되도록 함
//        setCanceledOnTouchOutside(true)

        with(binding) {
            txtDialogSubMessage.text = dialogSubMessage
            button1.text = button1Text
            button2.text = button2Text
            button1.setOnClickListener {
                dismiss()
            }
            button2.setOnClickListener {
                // 인터페이스르 통해 다이얼로그를 호출한 액티비티나 프래그먼트에 값을 전달한다.
                itemClickListener.onClick()
                dismiss()
            }
            binding.txtDialogMessage.text = message
        }
        return view
    }
}
