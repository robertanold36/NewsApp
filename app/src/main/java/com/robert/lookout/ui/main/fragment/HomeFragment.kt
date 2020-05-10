package com.robert.lookout.ui.main.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.robert.lookout.ui.main.viewModel.ContainerWebView
import com.robert.lookout.ui.main.util.Resources
import com.robert.lookout.ui.main.viewModel.MainActivity
import com.robert.lookout.ui.main.viewModel.PageViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject

/**
 * A placeholder fragment containing a simple view.
 */

class HomeFragment : Fragment(R.layout.fragment_main) {

    @Inject
    lateinit var viewModel: PageViewModel

    lateinit var newsAdapter: LookOutAdapter
    private val TAG = "HomeFragment"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).mainActivityComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //viewModel = (activity as MainActivity).viewModel

        initRecyclerView()

        newsAdapter.setOnClickListener {

            val intent = Intent(activity, ContainerWebView::class.java)
            intent.putExtra("article", it.url)
            intent.putExtra("article_name",it.source?.name)
            startActivity(intent)

        }

        viewModel.topHeadLineNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resources.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles.toList())
                        val totalPage = it.totalResults / 20 + 2
                        val isAtLastPage = viewModel.breakingPage == totalPage

                        if (isAtLastPage) {
                            progressBar.setPadding(0, 0, 0, 0)
                        }
                    }
                }

                is Resources.Loading -> {
                    showProgressBar()
                }

                is Resources.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Log.d(TAG, "THERE IS AN ERROR $it")
                    }
                }
            }
        })

    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
        isLoading = false
    }


    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
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
            val isNotAtBegging = firstItemVisible >= 0
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBegging &&
                        isScrolling && isTotalMoreThanVisible

            if (shouldPaginate) {
                viewModel.getNews("us")
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
        newsAdapter = LookOutAdapter()
        rv_news.apply {
            adapter = newsAdapter
            addOnScrollListener(scrollListener)
        }
    }
}