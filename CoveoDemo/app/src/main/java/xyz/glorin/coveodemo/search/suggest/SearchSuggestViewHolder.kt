package xyz.glorin.coveodemo.search.suggest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import xyz.glorin.coveodemo.R
import xyz.glorin.coveodemo.model.SearchSuggest

class SearchSuggestViewHolder(itemView: View, private val listener: SuggestClickListener) :
    RecyclerView.ViewHolder(itemView) {
    private val tvKeyword = itemView.findViewById<TextView>(R.id.tvKeyword)

    fun bind(model: SearchSuggest) {
        tvKeyword.text = model.expression ?: "Unknown"
        itemView.setOnClickListener {
            listener.onSuggestClicked(model)
        }
    }

    companion object {
        fun create(parent: ViewGroup, listener: SuggestClickListener): SearchSuggestViewHolder {
            return SearchSuggestViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.search_suggest_item, parent, false),
                listener
            )
        }
    }
}