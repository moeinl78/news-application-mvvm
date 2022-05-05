package ir.ariyana.news_application_mvvm.ui.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ir.ariyana.news_application_mvvm.repository.RepositoryMain

class ViewModelMainFactory(private val app : Application, private val repositoryMain: RepositoryMain) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ViewModelMain(app, repositoryMain) as T
    }
}