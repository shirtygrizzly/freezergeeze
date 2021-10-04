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
import com.example.freezergeez.databinding.FragmentFreezerTwoBinding
import com.google.firebase.database.*


class FreezerTwo :Fragment() {
    private lateinit var itemRview :RecyclerView
    private lateinit var itemArrayList:ArrayList<Item>
    private lateinit var myRef : DatabaseReference
    private var _binding:FragmentFreezerTwoBinding? =null
    private val binding get() = _binding!!

    private val sharedView : SharedView by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFreezerTwoBinding.inflate(inflater,container,false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Start up the Recycler View for the Fragment
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
        //Listen for Data Changes and update accordingly
        rootRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(itemSnapshot in snapshot.children){
                        val item =itemSnapshot.getValue(Item::class.java)
                        if(item!!.freezerLoc =="Freezer Two")
                            itemArrayList.add(item)
                    }
                    itemRview.adapter =RvAdaptor(itemArrayList){item : Item -> itemClicked((item))}
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })

    }
    //Function to get the values of the item that was clicked and share it to a ViewModel
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





