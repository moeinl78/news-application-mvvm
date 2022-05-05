package ir.ariyana.news_application_mvvm.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.ariyana.news_application_mvvm.R
import ir.ariyana.news_application_mvvm.databinding.FragmentSearchBinding
import ir.ariyana.news_application_mvvm.repository.model.Article
import ir.ariyana.news_application_mvvm.repository.model.NewDataClass
import ir.ariyana.news_application_mvvm.ui.main.ViewModelMain
import ir.ariyana.news_application_mvvm.ui.adapters.AdapterNews
import ir.ariyana.news_application_mvvm.utils.Constants
import ir.ariyana.news_application_mvvm.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FragmentSearch : Fragment(), AdapterNews.Events {

    private lateinit var binding : FragmentSearchBinding
    private lateinit var viewModelMain: ViewModelMain
    private lateinit var adapterSearch : AdapterNews

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModelMain = ViewModelProvider(requireActivity())[ViewModelMain::class.java]
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        var job : Job?= null

        binding.fragmentSearchInputQuery.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(1000L)
                editable?.let {
                    if(editable.toString().isNotEmpty()) {
                        viewModelMain.getSearchedNews(editable.toString())
                    }
                }
            }
        }

        viewModelMain
            .searchNewsData
            .observe(viewLifecycleOwner, Observer { response ->

                when(response) {

                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let {
                            adapterSearch.differ.submitList(it.articles.toList())
                            val totalPages = it.totalResults / Constants.QUERY_PAGE_SIZE + 2
                            isLastPage = viewModelMain.searchNewsPage == totalPages
                        }
                    }

                    is Resource.Error -> {
                        hideProgressBar()
                        Log.e(Constants.TAG_MAIN, response.message!!)
                    }

                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            })
    }

    private fun hideProgressBar() {
        binding.fragmentSearchProgressBar.visibility = View.GONE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.fragmentSearchProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    override fun onItemClick(article: NewDataClass.Article) {
        val bundle = Bundle().apply {
            putSerializable(Constants.BUNDLE_KEY, article)
        }
        findNavController().navigate(R.id.action_fragmentSearch_to_fragmentArticle, bundle)
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
                    .getSearchedNews("us")
                isScrolling = false
            }
        }
    }

    private fun setupRecyclerView() {
        val rv = binding.fragmentSearchRecyclerView
        adapterSearch = AdapterNews(this)
        rv.adapter = adapterSearch
        rv.layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.VERTICAL, false)
        rv.addOnScrollListener(this@FragmentSearch.scrollListener)
    }
}