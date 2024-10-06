package com.teamfilmo.filmo.ui.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import com.teamfilmo.filmo.databinding.WidgetCustomDialogBinding

interface ItemClickListener {
    fun onClick()
}

class CustomDialog(context: Context) : Dialog(context) {
    private lateinit var itemClickListener: ItemClickListener
    private lateinit var binding: WidgetCustomDialogBinding

    fun setItemClickListener(listener: ItemClickListener) {
        this.itemClickListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = WidgetCustomDialogBinding.inflate(LayoutInflater.from(context))

        // 다이얼로그를 둥글게 표현하기 위해 필요
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.requestFeature(Window.FEATURE_NO_TITLE)

        setContentView(binding.root)

        // 다이얼로그 바깥쪽 클릭 시 종료되도록 함
        setCanceledOnTouchOutside(true)

        // 취소 버튼 클릭 시 종료
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        binding.btnDelete.setOnClickListener {
            // 인터페이스르 통해 다이얼로그를 호출한 액티비티나 프래그먼트에 값을 전달한다.
            itemClickListener.onClick()
            dismiss()
        }
    }
}
