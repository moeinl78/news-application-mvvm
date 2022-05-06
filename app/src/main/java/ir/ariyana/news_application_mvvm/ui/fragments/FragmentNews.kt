package ir.ariyana.news_application_mvvm.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ir.ariyana.news_application_mvvm.R
import ir.ariyana.news_application_mvvm.databinding.FragmentBreakingNewsBinding
import ir.ariyana.news_application_mvvm.repository.model.Article
import ir.ariyana.news_application_mvvm.ui.main.ViewModelMain
import ir.ariyana.news_application_mvvm.ui.adapters.AdapterNews
import ir.ariyana.news_application_mvvm.utils.Constants
import ir.ariyana.news_application_mvvm.utils.Resource

class FragmentNews : Fragment(), AdapterNews.Events {

    private lateinit var binding : FragmentBreakingNewsBinding
    private lateinit var viewModelMain: ViewModelMain
    private lateinit var adapter : AdapterNews

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBreakingNewsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModelMain = ViewModelProvider(requireActivity())[ViewModelMain::class.java]
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewModelMain.breakingNewsData.observe(viewLifecycleOwner, Observer { response ->

            when(response) {

                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        adapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModelMain.breakingNewsPage == totalPages
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Snackbar
                            .make(view, it, Snackbar.LENGTH_LONG)
                            .show()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        binding.fragmentNewsProgressBar.visibility = View.GONE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.fragmentNewsProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    override fun onItemClick(article: Article) {
        val bundle = Bundle().apply {
            putSerializable(Constants.BUNDLE_KEY, article)
        }
        findNavController().navigate(R.id.action_fragmentNews_to_fragmentArticle, bundle)
    }

    /**
     * do some calculations to find out
     * if user is loading new data, scrolling or reached last item
     */
    var isLoading = false
    var isScrolling = false
    var isLastPage = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading and !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE

            val shouldPaginate = isNotLoadingAndNotLastPage and isAtLastItem and isNotAtBeginning and isTotalMoreThanVisible

            if(shouldPaginate) {
                viewModelMain
                    .getBreakingNews("us")
                isScrolling = false
            }
        }
    }

    private fun setupRecyclerView() {
        val rv = binding.fragmentNewsRecyclerView
        adapter = AdapterNews(this)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.VERTICAL, false)
        rv.addOnScrollListener(this@FragmentNews.scrollListener)
    }
}