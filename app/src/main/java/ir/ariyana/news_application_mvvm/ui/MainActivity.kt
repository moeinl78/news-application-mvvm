package ir.ariyana.news_application_mvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ir.ariyana.news_application_mvvm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}