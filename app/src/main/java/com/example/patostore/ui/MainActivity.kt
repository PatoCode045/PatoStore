package com.example.patostore.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.patostore.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}