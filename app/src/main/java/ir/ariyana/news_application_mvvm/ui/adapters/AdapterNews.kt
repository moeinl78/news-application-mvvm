package ir.ariyana.news_application_mvvm.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.ariyana.news_application_mvvm.databinding.ItemRecyclerBinding
import ir.ariyana.news_application_mvvm.repository.model.Article
import ir.ariyana.news_application_mvvm.repository.model.NewDataClass
import ir.ariyana.news_application_mvvm.utils.Constants.TAG_MAIN

class AdapterNews(private val events: Events) : RecyclerView.Adapter<AdapterNews.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NewDataClass.Article) {
            binding.itemRecyclerTitleText.text = item.title
            binding.itemRecyclerContentText.text = item.content
            binding.itemRecyclerTextSource.text = item.source.name
            binding.itemRecyclerTextDate.text = item.publishedAt
            Glide
                .with(binding.root.context)
                .load(item.urlToImage)
                .into(binding.itemRecyclerImageNews)

            itemView.setOnClickListener {
                events.onItemClick(item)
            }
        }
    }

    /**
     * to lazy load items in a efficient way
     * you need to use diffUtil
     * to compare data and see which one's are new!
     */
    private val differCallback = object : DiffUtil.ItemCallback<NewDataClass.Article>() {

        override fun areItemsTheSame(
            oldItem: NewDataClass.Article,
            newItem: NewDataClass.Article
        ): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(
            oldItem: NewDataClass.Article,
            newItem: NewDataClass.Article
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differCallback2 = object : DiffUtil.ItemCallback<Article>() {

        override fun areItemsTheSame(
            oldItem: Article,
            newItem: Article
        ): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(
            oldItem: Article,
            newItem: Article
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
    val differ2 = AsyncListDiffer(this, differCallback2)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        Log.i(TAG_MAIN, differ.currentList.size.toString())
        return differ.currentList.size
    }

    interface Events {
        fun onItemClick(article : NewDataClass.Article)
    }
}