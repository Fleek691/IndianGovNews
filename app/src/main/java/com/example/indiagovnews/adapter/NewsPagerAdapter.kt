package com.example.indiagovnews.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.indiagovnews.fragments.NewsFragment

class NewsPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    private val categories = listOf("all", "education", "health", "economy")
    private val titles = listOf("All News", "Education", "Health", "Economy")

    override fun getItemCount(): Int = categories.size

    override fun createFragment(position: Int): Fragment {
        return NewsFragment.newInstance(categories[position])
    }

    fun getTitle(position: Int): String = titles[position]
}
