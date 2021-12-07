package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.ItemListBinding
import com.udacity.asteroidradar.databinding.ItemListBindingImpl

class ItemAdapter : ListAdapter<Asteroid, ItemAdapter.ItemViewHolder>(DiffCallback) {

    class ItemViewHolder(private var binding: ItemListBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(item: Asteroid?) {
           itemView.setOnClickListener{
               val action = MainFragmentDirections.actionShowDetail(item!!)
               it.findNavController().navigate(action)
           }
            binding.asteroid = item
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>(){
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemListBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }

}