package com.teamfilmo.filmo.ui.write.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.teamfilmo.filmo.databinding.ItemLoadingBinding
import com.teamfilmo.filmo.databinding.MoviePosterItemBinding
import com.teamfilmo.filmo.ui.write.adapter.MoviePosterAdapter.MoviePosterViewHolder
import timber.log.Timber

class MoviePosterAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var posterUriList: MutableList<String> = arrayListOf()
    private var selectedPosition: Int? = null
    private var isLoading = false
    private var isLastPage = false

    companion object {
        const val VIEW_TYPE_ITEM = 0
        const val VIEW_TYPE_LOADING = 1
    }

    fun isLastPage() {
        isLastPage = true
    }

    fun setLoading(isLoading: Boolean) {
        this.isLoading = isLoading
    }

    fun setPosterUriList(uriList: List<String>) {
        posterUriList.clear()
        posterUriList.addAll(uriList)
        notifyDataSetChanged()
    }

    fun initializePosterUriList() {
        posterUriList = emptyList<String>().toMutableList()
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(
            position: Int,
            uri: String?,
        )
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == posterUriList.size - 1 && !isLastPage) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val binding = MoviePosterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MoviePosterViewHolder(binding)
            }
            VIEW_TYPE_LOADING -> {
                val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MovieLoadingViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int = posterUriList.size

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        if (holder is MoviePosterViewHolder) {
            holder.bind()
        } else {
        }
    }

    inner class MoviePosterViewHolder(private val binding: MoviePosterItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.ivMoviePoster.setOnClickListener {
                Timber.d("clicked : $position")
                selectedPosition = position
                onItemClickListener?.onItemClick(position, posterUriList[position])
            }
        }

        fun bind() {
            Glide.with(context)
                .load(posterUriList[position])
                .into(binding.ivMoviePoster)
        }
    }

    inner class MovieLoadingViewHolder(private val binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root)

    inner class FooterViewHolder(private val binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root)
}
