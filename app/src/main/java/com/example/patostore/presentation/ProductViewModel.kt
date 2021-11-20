package com.example.patostore.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.patostore.network.ProductService


class ProductViewModel (private val service: ProductService): ViewModel() {

    var favorites = MutableLiveData(mutableListOf<String>())

    fun fetchFavoriteList(list: List<String>){

        if (list.isNotEmpty()){
            favorites.value = list as MutableList<String>
        }
        Log.d("pato", "favoritos: ${favorites.value.toString()}")
    }

    fun insertIntoFavorite(id: String){
        val list = favorites.value
        list?.contains(id)?.let {
            if (!it){
                list?.add(id)
                favorites.value = list
                Log.d("pato", "se inserto en la lista: ${favorites.value.toString()}")
            }
        }

    }

    fun removeFromFavorites(id: String){
        val list = favorites.value
        list?.contains(id)?.let {
            if (it){
                list?.remove(id)
                favorites.value = list
                Log.d("pato", "se removio de la lista: ${favorites.value.toString()}")
            }
        }
    }

}

class ProductViewModelFactory(private val service: ProductService): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ProductService::class.java).newInstance(service)
    }

}