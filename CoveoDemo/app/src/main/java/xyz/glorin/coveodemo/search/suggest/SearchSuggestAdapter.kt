package xyz.glorin.coveodemo.search.suggest

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.glorin.coveodemo.model.SearchSuggest

class SearchSuggestAdapter(private val listener: SuggestClickListener) :
    RecyclerView.Adapter<SearchSuggestViewHolder>() {
    private val searchSuggests = mutableListOf<SearchSuggest>()

    fun setData(data: List<SearchSuggest>) {
        searchSuggests.clear()
        searchSuggests.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSuggestViewHolder {
        return SearchSuggestViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: SearchSuggestViewHolder, position: Int) {
        holder.bind(searchSuggests[position])
    }

    override fun getItemCount(): Int {
        return searchSuggests.size
    }
}