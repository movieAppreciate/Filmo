package com.teamfilmo.filmo.ui.report

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.ui.model.report.ReportItem

class ReportListViewAdapter(val context: Context, val list: List<ReportItem>) : BaseAdapter() {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        TODO("Not yet implemented")
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?,
    ): View {
        var view = convertView
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false)
        }

        val title = view!!.findViewById<TextView>(R.id.tv_title)
        val content = view.findViewById<TextView>(R.id.tv_content)
        val nickName = view.findViewById<TextView>(R.id.tv_nick_name)
        val likeCount = view.findViewById<TextView>(R.id.tv_like_count)
        val replyCount = view.findViewById<TextView>(R.id.tv_reply_count)

        title.text = list[position].title
        content.text = list[position].content
        nickName.text = list[position].nickname
        likeCount.text = list[position].likeCount.toString()
        replyCount.text = list[position].replyCount.toString()

        return view
    }
}
