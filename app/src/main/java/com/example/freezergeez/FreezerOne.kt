package com.example.freezergeez

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freezergeez.MainActivity.Companion.userID
import com.example.freezergeez.databinding.FragmentHomeBinding
import com.google.firebase.database.*


class FreezerOne :Fragment() {
    private lateinit var itemRview :RecyclerView
    private lateinit var itemArrayList:ArrayList<Item>
    private lateinit var myRef : DatabaseReference
    private var _binding:FragmentHomeBinding? =null
    private val binding get() = _binding!!

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
        //Start up Recycler View for Fragment
        itemRview = binding.recyclerId
        itemRview.layoutManager = LinearLayoutManager(activity)
        itemRview.setHasFixedSize(true)
        itemArrayList = arrayListOf()
        getItemData()
    }
    //Get Item Data for the Freezer
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
    //Get information on the item that was clicked and share to a view model
    private fun itemClicked(item:Item){
        val itemName = item.itemName
        val itemDesc = item.itemDesc
        val itemQty = item.itemQty
        val itemUrl = item.photoURL
        val itemFreezer = item.freezerLoc
        sharedView.saveItem(itemName,itemDesc,itemQty,itemUrl, itemFreezer)

        (activity as MainActivity).showDialog()

    }
    // To avoid memory leaks we set the binding back to null
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}






