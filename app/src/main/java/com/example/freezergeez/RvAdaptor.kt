package com.example.freezergeez

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.freezergeez.databinding.FreezerItemBinding

class RvAdaptor(private val itemList:ArrayList<Item>, val clickListener :(Item) -> Unit) :RecyclerView.Adapter<RvAdaptor.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvAdaptor.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =FreezerItemBinding.inflate(inflater,parent,false)
       return MyViewHolder(binding)

    }


    override fun onBindViewHolder(holder: RvAdaptor.MyViewHolder, position: Int) {
        (holder as MyViewHolder).bind(itemList[position],clickListener)

           /*val currentItem = itemList[position]
           binding.itemName.text = currentItem.itemName
           holder.itemQty.text = currentItem.itemQty
            holder.itemDesc.text = currentItem.itemDesc*/

       // val url = currentItem.photoURL


    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class MyViewHolder(private val binding:FreezerItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item:Item,clickListener: (Item) -> Unit) {
            binding.itemName.text =item.itemName
            //val itemName: TextView = itemView.findViewById(R.id.itemName_)
            binding.itemQty.text = item.itemQty
            //val itemQty: TextView = itemView.findViewById(R.id.itemQty_)
            binding.itemDescriptionView.text = item.itemDesc
            //val itemDesc: TextView = itemView.findViewById(R.id.itemDescriptionView)
            itemView.setOnClickListener { clickListener(item) }
        }

    }}




