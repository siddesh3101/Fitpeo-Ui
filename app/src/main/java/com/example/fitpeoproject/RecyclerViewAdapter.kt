package com.example.fitpeoproject

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fitpeoproject.databinding.PeoplesItemBinding

class RecyclerViewAdapter(val imagesList: List<Drawable?>) : RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>(){
    class RecyclerViewHolder(val binding : PeoplesItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(image : Drawable){
        Glide.with(binding.root).load(image).into(binding.peopleImage)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(PeoplesItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val items = imagesList[position]
        holder.bind(items!!)
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }

}