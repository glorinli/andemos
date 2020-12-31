package xyz.glorin.coveodemo.search

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import xyz.glorin.coveodemo.KeyboardUtil
import xyz.glorin.coveodemo.R
import xyz.glorin.coveodemo.model.NetworkState
import xyz.glorin.coveodemo.model.SearchSuggest
import xyz.glorin.coveodemo.model.Status
import xyz.glorin.coveodemo.search.suggest.ISearchSuggestor
import xyz.glorin.coveodemo.search.suggest.ISearchSuggestorHost

class SearchActivity : AppCompatActivity(), ISearchSuggestorHost {
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var etKeyword: EditText
    private lateinit var rvSearchResults: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private var justSetKeyword = false
    private var suggestor: ISearchSuggestor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)

        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        initSearchBar()
        initSearchResults()
        initSearchSuggest()
    }

    private fun initSearchSuggest() {
        suggestor = supportFragmentManager.findFragmentByTag("searchSuggestFragment") as? ISearchSuggestor

        etKeyword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (justSetKeyword) {
                    justSetKeyword = false
                    return
                }

                suggestor?.loadSuggests(s?.toString()?.trim() ?: "")
            }

        })
    }

    private fun initSearchBar() {
        findViewById<View>(R.id.btnFireSearch).setOnClickListener {
            fireSearch()
        }

        etKeyword = findViewById<EditText>(R.id.etKeyword).apply {
            setOnEditorActionListener { v, actionId, event ->
                var consumed = false
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    fireSearch()
                    consumed = true
                }

                consumed
            }
        }
    }

    private fun fireSearch() {
        etKeyword.text.toString().trim().let {
            if (!TextUtils.isEmpty(it)) {
                if (searchViewModel.search(it)) {
                    rvSearchResults.scrollToPosition(0)
                    (rvSearchResults.adapter as? SearchResultsAdapter)?.submitList(null)
                    KeyboardUtil.hideKeyBoard(etKeyword)
                    suggestor?.hideSuggests()
                }
            }
        }
    }

    private fun initSearchResults() {
        val adapter = SearchResultsAdapter()
        searchViewModel.searchResults.observe(this) {
            adapter.submitList(it)
        }

        rvSearchResults = findViewById<RecyclerView>(R.id.rvSearchResults).apply {
            this.adapter = adapter
            layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                DividerItemDecoration(
                    this@SearchActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        swipeRefresh = findViewById(R.id.swipeRefresh)
        swipeRefresh.setOnRefreshListener {
            searchViewModel.refresh()
        }

        searchViewModel.refreshState.observe(this) {
            swipeRefresh.isRefreshing = it == NetworkState.LOADING
        }
    }

    override fun onClickSearchSuggest(item: SearchSuggest) {
        item.expression?.let {
            justSetKeyword = true
            etKeyword.setText(it, TextView.BufferType.EDITABLE)
            fireSearch()
        }
    }
}