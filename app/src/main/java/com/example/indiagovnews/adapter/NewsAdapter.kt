package com.example.indiagovnews.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.indiagovnews.databinding.ItemNewsBinding
import com.example.indiagovnews.model.NewsArticle
import java.text.SimpleDateFormat
import java.util.Locale

class NewsAdapter(
    private val onItemClick: (NewsArticle) -> Unit
) : ListAdapter<NewsArticle, NewsAdapter.NewsViewHolder>(NewsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)
        holder.itemView.setOnClickListener { onItemClick(article) }
    }

    class NewsViewHolder(
        private val binding: ItemNewsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        private val displayFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

        fun bind(article: NewsArticle) {
            binding.apply {
                newsTitle.text = article.title
                newsDescription.text = article.description
                newsSource.text = article.source.name
                

                try {
                    val date = dateFormat.parse(article.publishedAt)
                    newsDate.text = date?.let { displayFormat.format(it) } ?: article.publishedAt
                } catch (e: Exception) {
                    newsDate.text = article.publishedAt
                }

                newsLanguage.text = article.language?.uppercase() ?: "EN"
                newsRegion.text = article.region.takeIf { !it.isNullOrBlank() } ?: "National"

                categoryChip.text = article.category?.replaceFirstChar { 
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() 
                }

                article.sentiment?.let { sentiment ->
                    sentimentIndicator.setBackgroundColor(when {
                        sentiment > 0.2 -> 0xFF4CAF50.toInt()
                        sentiment < -0.2 -> 0xFFF44336.toInt()
                        else -> 0xFFFFEB3B.toInt()
                    })
                    sentimentIndicator.isVisible = true
                } ?: run {
                    sentimentIndicator.isVisible = false
                }


                if (!article.imageUrl.isNullOrEmpty()) {
                    newsImage.isVisible = true
                    Glide.with(newsImage)
                        .load(article.imageUrl)
                        .centerCrop()
                        .into(newsImage)
                } else {
                    newsImage.isVisible = false
                }
            }
        }
    }

    private class NewsDiffCallback : DiffUtil.ItemCallback<NewsArticle>() {
        override fun areItemsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean {
            return oldItem == newItem
        }
    }
}
