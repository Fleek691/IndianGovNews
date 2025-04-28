package com.example.indiagovnews

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.indiagovnews.adapter.NewsPagerAdapter
import com.example.indiagovnews.databinding.ActivityMainBinding
import com.example.indiagovnews.model.NewsCategory
import com.example.indiagovnews.utils.PreferencesManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var pagerAdapter: NewsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferencesManager = PreferencesManager(this)
        setupToolbar()
        setupViewPager()
        setupTabLayout()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_rate -> {
                showRatingDialog()
                true
            }
            R.id.action_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupViewPager() {
        pagerAdapter = NewsPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter
        
        // Restore last selected tab
        binding.viewPager.setCurrentItem(preferencesManager.lastTabPosition, false)
        
        // Save selected tab position
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                preferencesManager.lastTabPosition = position
            }
        })
    }

    private fun setupTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = NewsCategory.values()[position].name.replace("_", " ")
        }.attach()
    }

    private fun showRatingDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_rate_app, null)
        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .create()

        val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar)
        val feedbackInput = dialogView.findViewById<EditText>(R.id.feedbackInput)
        val submitButton = dialogView.findViewById<Button>(R.id.submitButton)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)

        submitButton.setOnClickListener {
            val rating = ratingBar.rating
            val feedback = feedbackInput.text.toString()
            
            // Here you would typically send the rating and feedback to your backend
            preferencesManager.appRating = rating
            if (feedback.isNotEmpty()) {
                preferencesManager.appFeedback = feedback
            }
            
            dialog.dismiss()
            showThankYouMessage(rating)
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showThankYouMessage(rating: Float) {
        val message = if (rating >= 4) {
            "Thank you for your great rating!"
        } else {
            "Thank you for your feedback. We'll work on improving."
        }
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun logout() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                preferencesManager.clearLoginData()
                startActivity(
                        Intent(this, LoginActivity::class.java))
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }
}