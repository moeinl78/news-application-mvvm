package ir.ariyana.news_application_mvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import ir.ariyana.news_application_mvvm.R
import ir.ariyana.news_application_mvvm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainActivityBottomNavigation.setOnItemSelectedListener { item ->

            when(item.itemId) {

                R.id.bottomMenuBreakingNews -> {

                }

                R.id.bottomMenuSearchNews -> {

                }

                R.id.bottomMenuSaveNews -> {

                }
            }
            true
        }
    }
}