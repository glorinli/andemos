package xyz.glorin.coveodemo.search

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import xyz.glorin.coveodemo.KeyboardUtil
import xyz.glorin.coveodemo.R
import xyz.glorin.coveodemo.model.NetworkState
import xyz.glorin.coveodemo.model.Status

class SearchActivity : AppCompatActivity() {
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var etKeyword: EditText
    private lateinit var rvSearchResults: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)

        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        initSearchBar()
        initSearchResults()
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
}