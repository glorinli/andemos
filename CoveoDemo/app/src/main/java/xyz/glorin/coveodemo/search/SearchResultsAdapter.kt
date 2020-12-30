package xyz.glorin.coveodemo.search

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import xyz.glorin.coveodemo.model.SearchResult

class SearchResultsAdapter :
    PagedListAdapter<SearchResult, SearchResultViewHolder>(SEARCH_RESULT_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        return SearchResultViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    public override fun getItem(position: Int): SearchResult? {
        return super.getItem(position)
    }

    companion object {
        val SEARCH_RESULT_COMPARATOR = object : DiffUtil.ItemCallback<SearchResult>() {
            override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
                return oldItem.uniqueId == newItem.uniqueId
            }

            override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
                return oldItem == newItem
            }

        }
    }
}