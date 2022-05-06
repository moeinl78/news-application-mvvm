package ir.ariyana.news_application_mvvm.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ir.ariyana.news_application_mvvm.R
import ir.ariyana.news_application_mvvm.databinding.FragmentBookmarkBinding
import ir.ariyana.news_application_mvvm.repository.model.Article
import ir.ariyana.news_application_mvvm.ui.adapters.AdapterNews
import ir.ariyana.news_application_mvvm.ui.main.ViewModelMain
import ir.ariyana.news_application_mvvm.utils.Constants

class FragmentBookmark : Fragment(), AdapterNews.Events {

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

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = adapter.differ.currentList[position]
                viewModelMain.removeArticle(article)
                Snackbar
                    .make(view, "article removed successfully from database", Snackbar.LENGTH_SHORT)
                    .setAction("Undo") {
                        viewModelMain.insertArticle(article)
                    }
                    .show()
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.fragmentBookmarkRecyclerView)
        }

        viewModelMain
            .receiveArticles()
            .observe(viewLifecycleOwner, Observer { articles ->
                adapter.differ.submitList(articles)
            })
    }

    private fun setupRecyclerView() {
        adapter = AdapterNews(this)
        binding.fragmentBookmarkRecyclerView.adapter = adapter
        binding.fragmentBookmarkRecyclerView.layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.VERTICAL, false)
    }

    override fun onItemClick(article: Article) {
        val bundle = Bundle().apply {
            putSerializable(Constants.BUNDLE_KEY, article)
        }
        findNavController().navigate(R.id.action_fragmentSearch_to_fragmentArticle, bundle)
    }
}