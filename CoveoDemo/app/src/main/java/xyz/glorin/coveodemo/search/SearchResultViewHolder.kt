package xyz.glorin.coveodemo.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import xyz.glorin.coveodemo.R
import xyz.glorin.coveodemo.model.SearchResult

class SearchResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvTitle = view.findViewById<TextView>(R.id.tvTitle)

    fun bind(item: SearchResult?) {
        tvTitle.text = item?.title ?: "Placeholder Item"
    }

    companion object {
        fun create(parent: ViewGroup): SearchResultViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.search_result_item, parent, false)
            return SearchResultViewHolder(itemView)
        }
    }
}