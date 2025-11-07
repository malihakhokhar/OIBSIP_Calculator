package com.example.calculator


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.calculator.databinding.ActivityThemeBinding
import androidx.core.content.edit

class ThemeActivity : AppCompatActivity() {

    lateinit var switchBinding : ActivityThemeBinding
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize binding
        switchBinding = ActivityThemeBinding.inflate(layoutInflater)
        val view = switchBinding.root
        setContentView(view)

        switchBinding.toolLayout.setNavigationOnClickListener {
            finish()
        }

        sharedPreferences = this.getSharedPreferences("Dark Theme", Context.MODE_PRIVATE)

        switchBinding.mySwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    putBoolean("switch", true)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    putBoolean("switch", false)
                }
            }
        }
    }
}