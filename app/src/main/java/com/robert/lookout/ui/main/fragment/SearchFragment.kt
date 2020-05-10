package com.robert.lookout.ui.main.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.robert.lookout.R
import com.robert.lookout.ui.main.MyApplication
import com.robert.lookout.ui.main.adapter.LookOutAdapter
import com.robert.lookout.ui.main.util.Constant.Companion.SEARCH_NEWS_TIME_DELAY
import com.robert.lookout.ui.main.util.Resources
import com.robert.lookout.ui.main.viewModel.ContainerWebView
import com.robert.lookout.ui.main.viewModel.MainActivity
import com.robert.lookout.ui.main.viewModel.PageViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchFragment : Fragment(R.layout.fragment_search) {

    @Inject
    lateinit var viewModel: PageViewModel

    lateinit var searchAdapter: LookOutAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).mainActivityComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        searchAdapter.setOnClickListener {
            val intent = Intent(activity, ContainerWebView::class.java)
            intent.putExtra("article", it.url)
            intent.putExtra("article_name", it.source?.name)
            startActivity(intent)

        }

        var job: Job? = null

        search_news.addTextChangeListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                job?.cancel()
                job = MainScope().launch {
                    delay(SEARCH_NEWS_TIME_DELAY)
                    s?.let {
                        if (s.toString().isNotEmpty()) {
                            viewModel.searchNews(s.toString())
                        }
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                job?.cancel()
                job = MainScope().launch {
                    delay(SEARCH_NEWS_TIME_DELAY)
                    s?.let {
                        if (s.toString().isNotEmpty()) {
                            viewModel.searchNews(s.toString())
                        }
                    }
                }
            }

        })

        viewModel.searchNewsData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resources.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        searchAdapter.differ.submitList(it.articles.toList())
                        val totalPage = it.totalResults / 20 + 2
                        val isAtLastPage = viewModel.searchBreakingPage == totalPage

                        if (isAtLastPage) {
                            rv_search.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resources.Loading -> {
                    showProgressBar()
                }
                is Resources.Error -> {
                    hideProgressBar()
                    Log.d("SearchNews", "error while requesting${response.message}")
                }
            }
        })


    }

    private fun hideProgressBar() {
        searchProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        searchProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLastPage = false
    var isLoading = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager

            val firstItemVisible = layoutManager.findFirstVisibleItemPosition()
            val visibleCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val isTotalMoreThanVisible = totalItemCount >= 20

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstItemVisible + visibleCount >= totalItemCount
            val isNotAtBeggining = firstItemVisible >= 0
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeggining &&
                        isScrolling && isTotalMoreThanVisible

            if (shouldPaginate) {
                viewModel.searchNews(search_news.text.toString())
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun initRecyclerView() {
        searchAdapter = LookOutAdapter()
        rv_search.apply {
            adapter = searchAdapter
            addOnScrollListener(scrollListener)
            setOnClickListener {

            }
        }
    }

}