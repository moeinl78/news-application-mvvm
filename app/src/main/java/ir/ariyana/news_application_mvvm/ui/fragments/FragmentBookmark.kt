package ir.ariyana.news_application_mvvm.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.ariyana.news_application_mvvm.databinding.FragmentBookmarkBinding
import ir.ariyana.news_application_mvvm.ui.adapters.AdapterNews
import ir.ariyana.news_application_mvvm.ui.main.ViewModelMain

class FragmentBookmark : Fragment() {

    private lateinit var binding : FragmentBookmarkBinding
    private lateinit var viewModelMain: ViewModelMain
    private lateinit var adapter : AdapterNews

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookmarkBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModelMain = ViewModelProvider(requireActivity())[ViewModelMain::class.java]
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = AdapterNews()
        binding.fragmentBookmarkRecyclerView.adapter = adapter
        binding.fragmentBookmarkRecyclerView.layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.VERTICAL, false)
    }
}