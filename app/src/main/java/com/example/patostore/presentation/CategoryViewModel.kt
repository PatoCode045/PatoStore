package com.example.patostore.presentation



import android.util.Log
import androidx.lifecycle.*
import com.example.patostore.domain.Category
import com.example.patostore.domain.HighlightApiResponse
import com.example.patostore.domain.ProductApiResponse
import com.example.patostore.network.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class CategoryViewModel(private val service: Service ): ViewModel() {

    var foundedCategory = MutableLiveData<Category?>()
    var highlightList = MutableLiveData<HighlightApiResponse>()
    var highlightProductList = MutableLiveData<List<ProductApiResponse>>()

    fun searchCategory(query: String){
        Log.d("pato", "se ejecuto la funcion")
        viewModelScope.launch {
            Log.d("pato", "se ejecuto la corutina")
            foundedCategory.value = searchCategoryOnService(query)
            foundedCategory.value?.let {
                highlightList.value = highlightListFromService(it.category_id)
            }
            highlightList.value?.let {
                var ids = ""
                it.content.forEach{
                    ids+= "${it.id},"
                }
                Log.e("pato", "la lista de ids es: ${ids}")
                highlightProductList.value = getProductsFromService(ids)
            }

        }
    }

    private suspend fun getProductsFromService(ids: String): List<ProductApiResponse>? {
        Log.d("pato", "se llamo la suspend function de product")
        val response = withContext(Dispatchers.IO){
            service.getProductList(ids)
        }


        if (response.isSuccessful){
            Log.d("pato", "la response de product fue exitosa, ${response.body()}")

            response.body()?.forEach { response ->
                response.body.position = highlightList.value?.content?.find {
                    it.id.equals(response.body.id)
                }?.position ?: -1
            }
            return response.body()
        }

        Log.e("pato", "la response de product es ${response.toString()}")
        return null
    }

    private suspend fun searchCategoryOnService(query: String) : Category?{
        Log.d("pato", "se llamo la suspend function de category")
        val response = withContext(Dispatchers.IO){
             service.getCategories("MLA", 1, query)
        }

        if (response.isSuccessful){
            Log.d("pato", "la response de category fue exitosa")
            return response.body()?.get(0)
        }

        Log.e("pato", "la response de category es ${response.toString()}")
        return null
    }

    private suspend fun highlightListFromService(categoryId: String): HighlightApiResponse? {
        Log.d("pato", "se llamo la suspend function de highlight")
        val response = withContext(Dispatchers.IO){
            service.getHighlight("MLA", categoryId)
        }

        if (response.isSuccessful){
            Log.d("pato", "la response de highlight fue exitosa ${response.body()}")
            return response.body()
        }

        Log.e("pato", "la response de highlight es ${response.toString()}")
        return null
    }

}

class CategoryViewModelFactory(private val service: Service): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Service::class.java).newInstance(service)
    }
}