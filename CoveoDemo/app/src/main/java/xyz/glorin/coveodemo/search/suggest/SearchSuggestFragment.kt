package xyz.glorin.coveodemo.search.suggest

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import xyz.glorin.coveodemo.R
import xyz.glorin.coveodemo.model.SearchSuggest
import java.lang.IllegalStateException

class SearchSuggestFragment : Fragment(), ISearchSuggestor {
    private lateinit var viewModel: SearchSuggestViewModel
    private lateinit var host: ISearchSuggestorHost
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(SearchSuggestViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        host = context as? ISearchSuggestorHost
            ?: throw IllegalStateException("Should put this fragment in an Activity implements ISearchSuggestorHost")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_search_suggests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchSuggestAdapter = SearchSuggestAdapter(object : SuggestClickListener {
            override fun onSuggestClicked(item: SearchSuggest) {
                host.onClickSearchSuggest(item)
                hideSuggests()
            }
        })

        recyclerView = view.findViewById<RecyclerView>(R.id.rvSearchSuggests).apply {
            adapter = searchSuggestAdapter
            visibility = View.INVISIBLE
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        viewModel.suggest.observe(this) {
            recyclerView.visibility = if (it.isEmpty()) View.INVISIBLE else View.VISIBLE
            searchSuggestAdapter.setData(it)
        }

        loadSuggests("")
    }

    override fun loadSuggests(query: String) {
        viewModel.loadSearchSuggests(query)
    }

    override fun hideSuggests() {
        recyclerView.visibility = View.INVISIBLE
    }
}