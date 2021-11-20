package com.example.patostore.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.patostore.databinding.ViewHolderProductBinding
import com.example.patostore.domain.ProductApiResponse
import com.squareup.picasso.Picasso

class FavoriteAdapter(val productList: List<ProductApiResponse>, val itemClickListener: OnItemClickListener): RecyclerView.Adapter<FavoriteViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: ProductApiResponse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ViewHolderProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = FavoriteViewHolder(binding)

        binding.bShowProduct.setOnClickListener {
            val position = viewHolder.adapterPosition
            itemClickListener.onItemClick(
                productList[position]
            )
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) = holder.bind(productList[position] )


    override fun getItemCount(): Int = productList.size

}

class FavoriteViewHolder(val binding: ViewHolderProductBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(productApiResponse: ProductApiResponse){
        binding.tvProductName.text = "${productApiResponse.body.title}"
        Picasso.get().load(productApiResponse.body.thumbnail).into(binding.ivProductImage)

    }
}