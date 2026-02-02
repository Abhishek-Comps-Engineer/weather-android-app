package com.abhishek.internships.identifier.skysnap.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.abhishek.internships.identifier.skysnap.R
import com.abhishek.internships.identifier.skysnap.databinding.ActivityGetStartedBinding
import com.abhishek.internships.identifier.skysnap.util.Constant

class GetStartedActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityGetStartedBinding.inflate(layoutInflater)
    }

    private val prefs by lazy {
        getSharedPreferences(Constant.PREFERENCE_NAME, MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        applyInsets()

        if (!isNewUser()) {
            navigateToMainActivity()
            return
        }

        binding.getStartedButton.setOnClickListener {
            setNewUser(false)
            navigateToMainActivity()
        }
    }

    private fun isNewUser(): Boolean =
        prefs.getBoolean(Constant.NEW_USER, true)

    private fun setNewUser(value: Boolean) {
        prefs.edit().putBoolean(Constant.NEW_USER, value).apply()
    }
    private fun navigateToMainActivity() {
        binding.getStartedButton.isEnabled = false
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun applyInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val padding = resources.getDimensionPixelSize(R.dimen.screen_padding)

            v.setPadding(
                systemBars.left + padding,
                systemBars.top + padding,
                systemBars.right + padding,
                systemBars.bottom + padding
            )
            insets
        }
    }

}