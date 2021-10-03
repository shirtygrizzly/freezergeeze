package com.example.freezergeez

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.freezergeez.MainActivity.Companion.userID
import com.example.freezergeez.databinding.FragmentAddItemToFreezerBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_add_item_to_freezer.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2


const val REQUEST_CODE = 13

private const val FILE_NAME ="photo.jpg"
lateinit var photoFile :File
private var photoURI: Uri? = null


class AddItemToFreezer : Fragment() {
    private lateinit var database: DatabaseReference
    private var _binding: FragmentAddItemToFreezerBinding? = null
    private val binding get() = _binding!!
    private val storage = FirebaseStorage.getInstance()
    private lateinit var itemName: String
    private lateinit var itemDescription: String
    private lateinit var itemQty: String
    private var url:Uri? = null
    private lateinit var freezer:String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddItemToFreezerBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.AddPhoto.setOnClickListener {

            val takepictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile(FILE_NAME)

            val fileProvider = FileProvider.getUriForFile(
                requireActivity(),
                "com.example.freezergeez.fileprovider",
                photoFile
            )
            photoURI = Uri.parse(photoFile.path)


            takepictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            if (takepictureIntent.resolveActivity(activity?.packageManager!!) != null) {

                startActivityForResult(takepictureIntent, REQUEST_CODE)
            } else {
                Toast.makeText(activity, "No camera Application Found", Toast.LENGTH_SHORT).show()
            }
        }
        binding.addButton.setOnClickListener {

            itemName = binding.itemNameTxt.text.toString()
            itemQty = binding.itemQty.text.toString()
            itemDescription = binding.itemDesc.text.toString()
            fun uploadToFirebase(){
            binding.takenImageDisplay.isDrawingCacheEnabled = true
            binding.takenImageDisplay.buildDrawingCache()
            val bitmap = (binding.takenImageDisplay.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val data = baos.toByteArray()


            val path = "freezergeeze/" + UUID.randomUUID() + ".png"

            val storageRef = storage.getReference(path)
            val uploadTask = storageRef.putBytes(data)

            uploadTask.addOnProgressListener { (bytesTransferred, totalByteCount)->
                // Task completed successfully
                binding.progressBar.isVisible
                binding.addButton.isEnabled=false
                val progress = (100.0 * bytesTransferred) / totalByteCount
                Log.d(TAG, "Upload is $progress% done")
            }.addOnPausedListener {
                Log.d(TAG, "Upload is paused")
            }   .addOnFailureListener {
                Toast.makeText(activity, "Image Upload Failed", Toast.LENGTH_LONG).show()
            }.addOnCompleteListener{
                binding.progressBar.isGone
                binding.addButton.isEnabled=true
                storageRef.downloadUrl.addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        url = task.result
                    updateDatabase()}
                else Toast.makeText(activity, "Could not get URL", Toast.LENGTH_LONG).show()


            }
            }}



            val clicksListener = object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val freezer = parent?.getItemAtPosition(position) as String

                }
            }
            uploadToFirebase()


        }}


        private fun getPhotoFile(fileName: String): File {
            val storageDirectory = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File.createTempFile(fileName, ".jpg", storageDirectory)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {

                val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)


                binding.takenImageDisplay.setImageBitmap(takenImage)

            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }


        }

        private fun getValues(): String {
            return FreezerSelection.selectedItem.toString()
        }
private fun updateDatabase(){
    database = FirebaseDatabase.getInstance().getReference("User")
    freezer = getValues()

    val item = Item(itemName, itemQty, itemDescription, freezer, url.toString())

    database.child(userID).child(itemName).setValue(item)
        .addOnSuccessListener {
            binding.itemNameTxt.text.clear()
            binding.itemDesc.text.clear()
            binding.itemQty.text.clear()


            Toast.makeText(activity, "Item Added to Freezer", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show()
        }
}
    }















