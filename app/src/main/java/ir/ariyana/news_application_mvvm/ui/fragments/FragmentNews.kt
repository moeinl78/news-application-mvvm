package ir.ariyana.news_application_mvvm.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.ariyana.news_application_mvvm.databinding.FragmentBreakingNewsBinding
import ir.ariyana.news_application_mvvm.ui.main.ViewModelMain
import ir.ariyana.news_application_mvvm.ui.adapters.AdapterNews
import ir.ariyana.news_application_mvvm.utils.Constants
import ir.ariyana.news_application_mvvm.utils.Resource

class FragmentNews : Fragment() {

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
                        Log.i(Constants.TAG_MAIN, newsResponse.toString())
                        adapter.differ.submitList(newsResponse.articles.toMutableList())
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Log.e(Constants.TAG_MAIN, it)
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun setupRecyclerView() {
        adapter = AdapterNews()
        binding.fragmentNewsRecyclerView.adapter = adapter
        binding.fragmentNewsRecyclerView.layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.VERTICAL, false)
    }

    private fun hideProgressBar() {
        binding.fragmentNewsProgressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.fragmentNewsProgressBar.visibility = View.VISIBLE
    }
}