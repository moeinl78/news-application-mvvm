package ir.ariyana.news_application_mvvm.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import ir.ariyana.news_application_mvvm.ui.main.ViewModelMain
import ir.ariyana.news_application_mvvm.ui.adapters.AdapterNews
import ir.ariyana.news_application_mvvm.utils.Constants
import ir.ariyana.news_application_mvvm.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FragmentSearch : Fragment() {

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

        adapterSearch.setArticleClickListener { article ->
            val bundle = Bundle().apply {
                putSerializable(Constants.BUNDLE_KEY, article)
            }
            findNavController().navigate(R.id.action_fragmentSearch_to_fragmentArticle, bundle)
        }

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
                            Log.e(Constants.TAG_MAIN, it.toString())
                            adapterSearch.differ.submitList(it.articles)
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

    private fun setupRecyclerView() {
        adapterSearch = AdapterNews()
        binding.fragmentSearchRecyclerView.adapter = adapterSearch
        binding.fragmentSearchRecyclerView.layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.VERTICAL, false)
    }

    private fun hideProgressBar() {
        binding.fragmentSearchProgressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.fragmentSearchProgressBar.visibility = View.VISIBLE
    }
}