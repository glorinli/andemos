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
    private val tvSubtitle = view.findViewById<TextView>(R.id.tvSubtitle)

    fun bind(item: SearchResult?) {
        tvTitle.text = item?.title ?: "Placeholder Item"
        tvSubtitle.text = item?.excerpt ?: "Placeholder Item Subtitle"

        itemView.setOnClickListener {
            item?.let {
                SearchHelper.openSearchResult(it, itemView.context)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): SearchResultViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.search_result_item, parent, false)
            return SearchResultViewHolder(itemView)
        }
    }
}