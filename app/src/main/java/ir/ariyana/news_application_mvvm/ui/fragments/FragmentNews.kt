package ir.ariyana.news_application_mvvm.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.ariyana.news_application_mvvm.databinding.FragmentBreakingNewsBinding
import ir.ariyana.news_application_mvvm.ui.ViewModelMain
import ir.ariyana.news_application_mvvm.ui.adapters.AdapterNews

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

        viewModelMain.getBreakingNews("us")

    }

    private fun setupRecyclerView() {
        adapter = AdapterNews()
        binding.fragmentSearchRecyclerView.apply {
            adapter = adapter
            layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.VERTICAL, false)
        }
    }

    private fun hideProgressBar() {

    }

    private fun showProgressBar() {

    }
}