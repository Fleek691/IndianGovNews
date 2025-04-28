package com.example.indiagovnews

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.example.indiagovnews.databinding.ActivityLoginBinding
import com.example.indiagovnews.utils.PreferencesManager
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var preferencesManager: PreferencesManager

    private val indianStates = listOf(
        "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
        "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka",
        "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram",
        "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu",
        "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal",
        "Andaman and Nicobar Islands", "Chandigarh", "Dadra and Nagar Haveli",
        "Daman and Diu", "Delhi", "Jammu and Kashmir", "Ladakh", "Lakshadweep",
        "Puducherry"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferencesManager = PreferencesManager(this)

        setupStateDropdown()
        setupLoginButton()
        restoreUserData()
    }

    private fun setupStateDropdown() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, indianStates)
        (binding.stateLayout.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun setupLoginButton() {
        binding.loginButton.setOnClickListener {
            val name = binding.nameInput.text.toString()
            val email = binding.emailInput.text.toString()
            val state = binding.stateDropdown.text.toString()

            if (validateInputs(name, email, state)) {
                saveUserData(name, email, state)
                startMainActivity()
            }
        }
    }

    private fun validateInputs(name: String, email: String, state: String): Boolean {
        if (name.isBlank()) {
            binding.nameLayout.error = "Please enter your name"
            return false
        }
        binding.nameLayout.error = null

        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailLayout.error = "Please enter a valid email address"
            return false
        }
        binding.emailLayout.error = null

        if (state.isBlank() || state !in indianStates) {
            binding.stateLayout.error = "Please select your state"
            return false
        }
        binding.stateLayout.error = null

        return true
    }

    private fun saveUserData(name: String, email: String, state: String) {
        if (binding.rememberMeCheckbox.isChecked) {
            preferencesManager.apply {
                userName = name
                userEmail = email
                userState = state
                isLoggedIn = true
            }
        }
    }

    private fun restoreUserData() {
        if (preferencesManager.isLoggedIn) {
            binding.nameInput.setText(preferencesManager.userName)
            binding.emailInput.setText(preferencesManager.userEmail)
            binding.stateDropdown.setText(preferencesManager.userState, false)
            binding.rememberMeCheckbox.isChecked = true
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
