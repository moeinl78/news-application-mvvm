package ir.ariyana.news_application_mvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import ir.ariyana.news_application_mvvm.R
import ir.ariyana.news_application_mvvm.databinding.ActivityMainBinding
import ir.ariyana.news_application_mvvm.repository.RepositoryMain
import ir.ariyana.news_application_mvvm.repository.local.DatabaseNews

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModelMain: ViewModelMain

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repositoryMain = RepositoryMain(DatabaseNews(this))
        val viewModelMainFactory = ViewModelMainFactory(repositoryMain)

        viewModelMain = ViewModelProvider(this, viewModelMainFactory)[ViewModelMain::class.java]

        val bottomNavigation = binding.mainActivityBottomNavigation
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainActivityFragmentHost)

        NavigationUI
            .setupWithNavController(bottomNavigation, navHostFragment!!.findNavController())
    }
}