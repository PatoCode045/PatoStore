package com.example.patostore.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.patostore.domain.ProductApiResponse
import com.example.patostore.network.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteViewModel (private val service: Service): ViewModel() {

    var favorites = mutableListOf<String>()
    var favoriteProductList = MutableLiveData<List<ProductApiResponse>>()

    fun fetchFavoriteList(list: List<String>){
        if (list.isNotEmpty()){
            favorites = list as MutableList<String>
            var ids = ""
            favorites.forEach{
                ids = "${ids}${it},"
            }
            viewModelScope.launch {
                favoriteProductList.value = getProductsFromService(ids)
            }
        }
        Log.d("pato", "favoritos: ${favorites.toString()}")
    }

    private suspend fun getProductsFromService(ids: String): List<ProductApiResponse>? {
        Log.d("pato", "se llamo la suspend function de product")
        val response = withContext(Dispatchers.IO){
            service.getProductList(ids)
        }

        if (response.isSuccessful){
            Log.d("pato", "la response de product fue exitosa, ${response.body()}")
            return response.body()
        }
        Log.e("pato", "la response de product es ${response.toString()}")
        return null
    }

}

class FavoriteViewModelFactory(private val service: Service): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Service::class.java).newInstance(service)
    }

}