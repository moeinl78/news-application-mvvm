package ir.ariyana.news_application_mvvm.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import ir.ariyana.news_application_mvvm.databinding.FragmentArticleBinding

class FragmentArticle : Fragment() {

    private lateinit var binding : FragmentArticleBinding
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

        val article = args.article
        binding.fragmentArticleWebView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }
    }
}