package ir.ariyana.news_application_mvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import ir.ariyana.news_application_mvvm.R
import ir.ariyana.news_application_mvvm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigation = binding.mainActivityBottomNavigation
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainActivityFragmentHost)

        NavigationUI
            .setupWithNavController(bottomNavigation, navHostFragment!!.findNavController())
    }
}