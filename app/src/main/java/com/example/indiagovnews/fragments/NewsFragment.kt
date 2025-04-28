package com.example.indiagovnews.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.indiagovnews.R
import com.example.indiagovnews.adapter.NewsAdapter
import com.example.indiagovnews.databinding.FragmentNewsBinding
import com.example.indiagovnews.di.AppModule
import com.example.indiagovnews.model.NewsCategory
import com.example.indiagovnews.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class NewsFragment : Fragment() {
    private var category: String = "all"
    private lateinit var binding: FragmentNewsBinding
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var viewModel: NewsViewModel

    companion object {
        private const val ARG_CATEGORY = "category"

        fun newInstance(category: String): NewsFragment {
            return NewsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CATEGORY, category)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getString(ARG_CATEGORY) ?: "all"
        }
        viewModel = ViewModelProvider(
            requireActivity(),
            AppModule.newsViewModelFactory
        )[NewsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        loadNews()
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter { article ->
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(context, "Could not open article", Toast.LENGTH_SHORT).show()
            }
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    val totalItemCount = layoutManager.itemCount
                    if (lastVisibleItem >= totalItemCount - 3) {
                        viewModel.loadMoreNews()
                    }
                }
            })
        }
    }

    private fun setupObservers() {
        viewModel.newsArticles.observe(viewLifecycleOwner) { articles ->
            newsAdapter.submitList(articles)
            binding.progressBar.isVisible = false
            if (articles.isEmpty()) {
                binding.emptyView.isVisible = true
            } else {
                binding.emptyView.isVisible = false
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun loadNews() {
        val newsCategory = when (category.lowercase()) {
            "education" -> NewsCategory.EDUCATION
            "health" -> NewsCategory.HEALTH
            "economy" -> NewsCategory.ECONOMY
            else -> NewsCategory.GENERAL
        }
        viewModel.loadNewsByCategory(newsCategory)
    }
}
