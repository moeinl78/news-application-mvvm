package ir.ariyana.news_application_mvvm.api

import ir.ariyana.news_application_mvvm.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ManagerAPI {

    companion object {

        private val retrofit by lazy {

            val logging = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient
                .Builder()
                .addInterceptor(logging)
                .build()

            Retrofit
                .Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        private val api by lazy {
            retrofit.create(ServiceAPI::class.java)
        }
    }
}