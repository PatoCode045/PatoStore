package com.example.patostore.network

import com.example.patostore.domain.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

const val API_KEY = "APP_USR-2169244178895327-112019-7bfec2cde0dae2c0381c4be86cdc52c1-144464632"
interface Service {

    //Function that accepts a COUNTRY_ID(MLA, MLC, MLO..) and a query to predict a possible
    //category as a response, by default the response its an array but we are only using the
    // first value. Limit only defines how many results you ask for.
    @Headers("Authorization: Bearer ${API_KEY}")
    @GET("sites/{COUNTRY_ID}/domain_discovery/search")
    suspend fun getCategories(@Path("COUNTRY_ID")siteId:String,
                              @Query("limit") limit: Int = 1,
                              @Query("q") searchTerm:String
    ) : Response<ArrayList<Category>>


    //Function that takes a COUNTRY_ID(MLA, MLC, MLO..) and a CATEGORY_ID and returns a list of
    //the top 20 (Highlight) products of that category
    @Headers("Authorization: Bearer ${API_KEY}")
    @GET("highlights/{COUNTRY_ID}/category/{CATEGORY_ID}")
    suspend fun getHighlight(@Path("COUNTRY_ID")siteId:String,
                             @Path("CATEGORY_ID") categoryId:String
    ) : Response<HighlightApiResponse>


    //Function that accepts a list of ids separated by comma and returns a list of products
    @Headers("Authorization: Bearer ${API_KEY}")
    @GET("/items")
    suspend fun getProductList(@Query("ids") productIdList: String): Response<List<ProductApiResponse>> //Response<Any> //


    //Function that by getting a product id returns the details of that particular product
    @Headers("Authorization: Bearer ${API_KEY}")
    @GET("/products/{PRODUCT_ID}")
    suspend fun getProductById(@Path("PRODUCT_ID")productId:String): Response<Details>


}