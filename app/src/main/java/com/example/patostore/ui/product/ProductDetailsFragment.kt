package com.example.patostore.ui.product

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import androidx.core.content.edit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.patostore.R
import com.example.patostore.databinding.FragmentProductDetailsBinding
import com.example.patostore.network.ProductService
import com.example.patostore.presentation.ProductViewModel
import com.example.patostore.presentation.ProductViewModelFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {

    lateinit var binding: FragmentProductDetailsBinding
    val args by navArgs<ProductDetailsFragmentArgs>()
    val gson = Gson()


    private val viewModel by viewModels<ProductViewModel> {
        ProductViewModelFactory(getRetrofit().create(ProductService::class.java))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = activity?.getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val list = sharedPref?.getString("list", "")?:""
        val listType = object : TypeToken<List<String>>() { }.type
        Log.d("pato", "se cargo la lista: ${list}")
        viewModel.fetchFavoriteList(gson.fromJson<List<String>>(list, listType)?: listOf())

        binding = FragmentProductDetailsBinding.bind(view)

        viewModel.getDetails(args.id)

        viewModel.productDetails.observe(viewLifecycleOwner, Observer {
            binding.tvTest.text = it.name
        })

        binding.bInsertIntoFavorites.setOnClickListener {
            viewModel.insertIntoFavorite( args.id )
            toggleFavoriteButton()
        }

        binding.bRemoveFromFavorites.setOnClickListener {
            viewModel.removeFromFavorites( args.id )
            toggleFavoriteButton()
        }

        viewModel.favorites.observe(viewLifecycleOwner, Observer {
            Log.d("pato", "se mando a actualizar: ${it.toString()}")
            updateList(it)
        })

    }

    fun updateList(list: List<String>){
        val sharedPref = activity?.getSharedPreferences("favorites", Context.MODE_PRIVATE)
        Log.d("pato", "se recibio para a actualizar: ${list}")
        sharedPref?.edit {
            Log.d("pato", "se actualizo la lista: ${list}")
            putString("list", gson.toJson(list))
            commit()
        }
    }

    fun toggleFavoriteButton(){
        if (binding.bRemoveFromFavorites.visibility == ImageView.VISIBLE){
            binding.bInsertIntoFavorites.visibility = ImageView.VISIBLE
            binding.bRemoveFromFavorites.visibility = ImageView.INVISIBLE
        }else{
            binding.bInsertIntoFavorites.visibility = ImageView.INVISIBLE
            binding.bRemoveFromFavorites.visibility = ImageView.VISIBLE
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://api.mercadolibre.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}