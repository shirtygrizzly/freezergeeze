package com.example.freezergeez

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.freezergeez.databinding.FreezerItemBinding
import com.squareup.picasso.Picasso

class RvAdaptor(private val itemList:ArrayList<Item>, private val clickListener :(Item) -> Unit) :RecyclerView.Adapter<RvAdaptor.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvAdaptor.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =FreezerItemBinding.inflate(inflater,parent,false)
        return MyViewHolder(binding)

    }

    //Bindings to make the values in the RecyclerView reachable
    override fun onBindViewHolder(holder: RvAdaptor.MyViewHolder, position: Int) {
        (holder as MyViewHolder).bind(itemList[position],clickListener)

    }
    //Get the size of the list
    override fun getItemCount(): Int {
        return itemList.size
    }
    //Implement click listener and get values
    class MyViewHolder(private val binding:FreezerItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item:Item,clickListener: (Item) -> Unit) {
            binding.itemName.text =item.itemName

            binding.itemQty.text = item.itemQty

            binding.itemDescriptionView.text = item.itemDesc

            Picasso.get().load(item.photoURL).into(binding.photo)
            itemView.setOnClickListener { clickListener(item) }
        }

    }

}




