package com.example.patostore.ui.category

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.patostore.R
import com.example.patostore.databinding.FragmentCategoryBinding
import com.example.patostore.domain.ProductApiResponse
import com.example.patostore.network.Service
import com.example.patostore.presentation.CategoryViewModel
import com.example.patostore.presentation.CategoryViewModelFactory
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class CategoryFragment : Fragment(R.layout.fragment_category),
    ProductAdapter.OnItemClickListener {

    lateinit var binding: FragmentCategoryBinding
    private val viewModel by viewModels<CategoryViewModel> {
        CategoryViewModelFactory(getRetrofit().create(Service::class.java))
    }
    val gson = Gson()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCategoryBinding.bind(view)

        binding.etQuery.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchCategory(it) }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        viewModel.foundedCategory.observe(viewLifecycleOwner, Observer {
            binding.tvCategory.text = it?.category_name
        })

        viewModel.highlightProductList.observe(viewLifecycleOwner, Observer {

            val adapter = ProductAdapter(it, this@CategoryFragment)
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
        val action = CategoryFragmentDirections.actionProductListFragmentToProductDetailsFragment(
            item.body.id,
            item.body.catalog_product_id,
            item.body.title,
            "$ ${item.body.price}(${item.body.currency_id})",
            gson.toJson(item.body.pictures)
            )
        findNavController().navigate(action)
    }
}