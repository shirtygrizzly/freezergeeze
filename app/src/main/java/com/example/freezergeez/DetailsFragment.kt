package com.example.freezergeez

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.freezergeez.databinding.FragmentDetailsBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

private lateinit var database: DatabaseReference
class DetailsFragment : DialogFragment() {
    private val sharedView : SharedView by activityViewModels()

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        sharedView.itemName.observe(viewLifecycleOwner,{ itemName ->
            binding.itemName.text= itemName })
        sharedView.itemDesc.observe(viewLifecycleOwner,{ itemDesc ->
            binding.description.setText(itemDesc)})
        sharedView.itemQty.observe(viewLifecycleOwner,{ itemQty ->
            binding.showItemQty.setText(itemQty)})

        val photoUrl = sharedView.itemUrl.value
        Picasso.get().load(photoUrl).into(binding.itemPhoto)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.updateBtn.setOnClickListener{
            showdialog(view)


        }
        binding.cancelBtn.setOnClickListener{
            dialog?.dismiss()
        }
    }
    fun showdialog(view: View) {

        val alertDialog = AlertDialog.Builder(activity)
            //set icon
            .setIcon(android.R.drawable.ic_dialog_alert)
            //set title
            .setTitle("Item details changed")
            //set message
            .setMessage("Are you sure you want to update this item?")
            //set positive button
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, i ->
                //set what would happen when positive button is clicked
                updateDatabase()

                Toast.makeText(activity, "Updating Database", Toast.LENGTH_LONG).show()
            })
            //set negative button
            .setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.cancel()
                //set what should happen when negative button is clicked
                Toast.makeText(activity, "Nothing Happened", Toast.LENGTH_LONG).show()
            })
            .show()
    }


    private fun updateDatabase(){
        database = FirebaseDatabase.getInstance().getReference("User")
        // If the user updates information send it to the database
        val itemName = binding.itemName.text.toString()
        val itemQty = binding.showItemQty.text.toString()
        val itemDescription =binding.description.text.toString()
        val freezer = sharedView.itemFreezer.value
        val url = sharedView.itemUrl.value

        val item = Item(itemName, itemQty, itemDescription, freezer, url)
        //if the user has changed the QTY to 0 remove it from the Database
        if(itemQty !="0") {
            database.child(MainActivity.userID).child(itemName).setValue(item)
        }else{
            database.child(MainActivity.userID).child(itemName).removeValue()

        }
    }
    // To avoid memory leaks we set the binding back to null
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }}


