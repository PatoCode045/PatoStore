package com.example.patostore.ui.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.patostore.databinding.ViewHolderCarouselBinding
import com.example.patostore.domain.Picture
import com.squareup.picasso.Picasso

class CarouselAdapter(val imageList: List<Picture>): RecyclerView.Adapter<CarouselViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding =
            ViewHolderCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) = holder.bind(imageList[position] )

    override fun getItemCount(): Int = imageList.size

}

class CarouselViewHolder(val binding: ViewHolderCarouselBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(picture: Picture){
        Picasso.get().load(picture.secure_url).into(binding.ivCarouselImage)
    }
}