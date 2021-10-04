package com.example.freezergeez

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freezergeez.MainActivity.Companion.userID
import com.example.freezergeez.databinding.FragmentHomeBinding
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.freezer_item.*


class FreezerOne :Fragment() {
    private lateinit var itemRview :RecyclerView
    private lateinit var itemArrayList:ArrayList<Item>
    private lateinit var myRef : DatabaseReference
    private lateinit var _binding:FragmentHomeBinding
    private val binding get() = _binding

private val sharedView : SharedView by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater,container,false)


        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)





    }
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            itemRview = binding.recyclerId
            itemRview.layoutManager = LinearLayoutManager(activity)
            itemRview.setHasFixedSize(true)
            itemArrayList = arrayListOf()
            getItemData()
            }

private fun getItemData(){
    myRef= FirebaseDatabase.getInstance().getReference("User")
    val rootRef = myRef.child(userID)
    rootRef.addValueEventListener(object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            if(snapshot.exists()){
                for(itemSnapshot in snapshot.children){
                    val item =itemSnapshot.getValue(Item::class.java)
                    if(item!!.freezerLoc =="Freezer One")
                    itemArrayList.add(item)
                }
                itemRview.adapter =RvAdaptor(itemArrayList){item : Item -> itemClicked((item))}
            }
        }

        override fun onCancelled(error: DatabaseError) {

        }


    })
}
    private fun itemClicked(item:Item){
        val itemName = item.itemName
        val itemDesc = item.itemDesc
        val itemQty = item.itemQty
        val itemUrl = item.photoURL
        sharedView.saveItem(itemName,itemDesc,itemQty,itemUrl)

        (activity as MainActivity).showDialog()



        Toast.makeText(activity, "Clicked: ${item.itemName}",Toast.LENGTH_SHORT).show()
    }
}






