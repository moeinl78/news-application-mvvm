package ir.ariyana.news_application_mvvm.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import ir.ariyana.news_application_mvvm.databinding.FragmentArticleBinding
import ir.ariyana.news_application_mvvm.repository.model.Article
import ir.ariyana.news_application_mvvm.ui.main.ViewModelMain

class FragmentArticle : Fragment() {

    private lateinit var binding : FragmentArticleBinding
    private lateinit var viewModelMain : ViewModelMain
    private val args : FragmentArticleArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelMain = ViewModelProvider(requireActivity())[ViewModelMain::class.java]

        val article = args.article
        binding.fragmentArticleWebView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }

        binding.fragmentArticleSaveFab.setOnClickListener {
            viewModelMain
                .insertArticle(article)

            Snackbar
                .make(view, "Saved to database Successfully", Snackbar.LENGTH_SHORT)
                .show()
        }
    }
}