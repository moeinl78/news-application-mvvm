package ir.ariyana.news_application_mvvm.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ir.ariyana.news_application_mvvm.R
import ir.ariyana.news_application_mvvm.databinding.FragmentBookmarkBinding
import ir.ariyana.news_application_mvvm.ui.ViewModelMain

class FragmentBookmark : Fragment() {

    private lateinit var binding : FragmentBookmarkBinding
    private lateinit var viewModelMain: ViewModelMain

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
    }
}