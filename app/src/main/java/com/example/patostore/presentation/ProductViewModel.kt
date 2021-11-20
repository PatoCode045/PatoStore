package com.example.patostore.presentation

import android.util.Log
import androidx.lifecycle.*
import com.example.patostore.domain.Details
import com.example.patostore.network.ProductService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher


class ProductViewModel (private val service: ProductService): ViewModel() {

    var favorites = MutableLiveData(mutableListOf<String>())
    var productDetails = MutableLiveData<Details>()



    fun getDetails(id:String){
        viewModelScope.launch {
            productDetails.value = getDetailsFromService(id)
        }
    }

    private suspend fun getDetailsFromService(id: String): Details? {
        val response = withContext(Dispatchers.IO){
            service.getProductById(id)
        }
        return response.body()
    }

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