package com.example.patostore.ui.category

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.patostore.R
import com.example.patostore.databinding.FragmentProductListBinding
import com.example.patostore.domain.ProductApiResponse
import com.example.patostore.network.ProductService
import com.example.patostore.presentation.CategoryViewModel
import com.example.patostore.presentation.CategoryViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ProductListFragment : Fragment(R.layout.fragment_product_list),
    ProductAdapter.OnItemClickListener {

    lateinit var binding: FragmentProductListBinding
    private val viewModel by viewModels<CategoryViewModel> {
        CategoryViewModelFactory(getRetrofit().create(ProductService::class.java))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProductListBinding.bind(view)

        binding.bSearch.setOnClickListener {
            Log.d("pato", "se presiono el boton")
            viewModel.searchCategory(binding.etQuery.text.toString())
        }

        binding.tvResult.setOnClickListener {
            findNavController().navigate(R.id.action_productListFragment_to_favoriteFragment)
        }

        viewModel.highlightProductList.observe(viewLifecycleOwner, Observer {

            val adapter = ProductAdapter(it, this@ProductListFragment)
            Log.d("pato", "la lista es ${it.toString()}")
            binding.rvProducts.adapter = adapter
        })

    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://api.mercadolibre.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override fun onItemClick(item: ProductApiResponse) {
        val action = ProductListFragmentDirections.actionProductListFragmentToProductDetailsFragment(
                item.body.title,
                item.body.id
            )
        findNavController().navigate(action)
    }
}