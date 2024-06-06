package com.teamfilmo.filmo.ui.report.recyclerview

import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

open class BaseReportAdapter<Model : Any, ViewHolder : RecyclerView.ViewHolder>(
    private val onCreateViewHolder: (parent: ViewGroup, viewType: Int) -> ViewHolder,
    private val onBindViewHolder: (viewHolder: ViewHolder, position: Int) -> Unit,
    private val onItemClickListener: ((Model, Int) -> Unit)? = null,
) : RecyclerView.Adapter<ViewHolder>() {
    val item = listOf<Model>()

    private val onClickListener: OnClickListener =
        OnClickListener {
            if (it.tag is Int) {
                val position = it.tag
                TODO()
            }
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder = onCreateViewHolder.invoke(parent, viewType)

    override fun getItemCount(): Int = item.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        onBindViewHolder.invoke(holder, position)

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(item[position], position)
        }
    }
}
