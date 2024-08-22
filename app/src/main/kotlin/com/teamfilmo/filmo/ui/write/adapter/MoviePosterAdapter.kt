package com.teamfilmo.filmo.ui.write.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import timber.log.Timber

class MoviePosterAdapter(private val context: Context) : BaseAdapter() {
    private var posterUriList: ArrayList<String> = arrayListOf()
    private var selectedPosition: Int? = null

    fun setPosterUriList(uriList: List<String>) {
        this.posterUriList.addAll(uriList)
        Timber.d("전달된 uri list $posterUriList")
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

    override fun getCount(): Int = posterUriList.size

    override fun getItem(position: Int): Any = posterUriList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?,
    ): View {
        var imageView = ImageView(context)
        if (convertView == null) {
            imageView.run {
                layoutParams = ViewGroup.LayoutParams(310, 400)
                scaleType = ImageView.ScaleType.FIT_CENTER
            }
        } else {
            imageView = convertView as ImageView
        }

        if (posterUriList[position] != "0") {
            Glide.with(context)
                .load(posterUriList[position])
                .into(imageView)
        }

        imageView.setOnClickListener {
            Timber.d("clicked : $position")
            selectedPosition = position
            onItemClickListener?.onItemClick(position, posterUriList[position])
        }

        return imageView
    }

    fun getSelectedMoviePosition(): Int? {
        return selectedPosition
    }
}
