package com.example.patostore.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.patostore.databinding.ViewHolderProductBinding
import com.example.patostore.domain.Product
import com.example.patostore.domain.ProductApiResponse
import com.squareup.picasso.Picasso

class ProductAdapter(val productList: List<ProductApiResponse>, val itemClickListener: OnItemClickListener): RecyclerView.Adapter<ProductViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: ProductApiResponse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ViewHolderProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ProductViewHolder(binding)

        binding.bShowProduct.setOnClickListener {
            val position = viewHolder.adapterPosition
            itemClickListener.onItemClick(
                productList.find {
                    it.body.position.equals(position+1)
                }!!
            )
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) = holder.bind(productList.find {
        it.body.position.equals(position + 1)
    }?: ProductApiResponse(0, Product("", "", "", 0)) )


    override fun getItemCount(): Int = productList.size

}

class ProductViewHolder(val binding: ViewHolderProductBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(productApiResponse: ProductApiResponse){
        binding.tvProductName.text = "${productApiResponse.body.position} - ${productApiResponse.body.title}"
        Picasso.get().load(productApiResponse.body.secure_thumbnail).into(binding.ivProductImage)

    }
}