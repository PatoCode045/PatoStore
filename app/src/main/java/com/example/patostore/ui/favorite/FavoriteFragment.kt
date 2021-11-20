package com.example.patostore.ui.favorite

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.patostore.R
import com.example.patostore.databinding.FragmentFavoriteBinding
import com.example.patostore.domain.ProductApiResponse
import com.example.patostore.network.ProductService
import com.example.patostore.presentation.FavoriteViewModel
import com.example.patostore.presentation.FavoriteViewModelFactory
import com.example.patostore.ui.category.ProductListFragmentDirections
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FavoriteFragment : Fragment(R.layout.fragment_favorite), FavoriteAdapter.OnItemClickListener {

    lateinit var binding: FragmentFavoriteBinding
    private val viewModel by viewModels<FavoriteViewModel> {
        FavoriteViewModelFactory(getRetrofit().create(ProductService::class.java))
    }

    val gson = Gson()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteBinding.bind(view)

        var sharedPref = activity?.getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val list = sharedPref?.getString("list", "")?:""
        val listType = object : TypeToken<List<String>>() { }.type
        Log.d("pato", "se cargo la lista: ${gson.fromJson<List<String>>(list,listType)}")
        viewModel.fetchFavoriteList(gson.fromJson<List<String>>(list,listType)?: listOf())

        viewModel.favoriteProductList.observe(viewLifecycleOwner, Observer {

            val adapter = FavoriteAdapter(it, this@FavoriteFragment)
            Log.d("pato", "la lista es ${it.toString()}")
            binding.rvFavorite.adapter = adapter
        })
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://api.mercadolibre.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override fun onItemClick(item: ProductApiResponse) {
        val action = FavoriteFragmentDirections.actionFavoriteFragmentToProductDetailsFragment(
            item.body.catalog_product_id
        )
        findNavController().navigate(action)
    }
}