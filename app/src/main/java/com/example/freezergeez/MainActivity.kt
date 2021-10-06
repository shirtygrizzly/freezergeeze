package com.example.freezergeez

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.freezergeez.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_add_item_to_freezer.*
import kotlinx.android.synthetic.main.main_screen.*
import java.io.File

lateinit var photoFile : File
class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    companion object{
        lateinit var userID:String
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var layout: View
    private lateinit var binding: ActivityMainBinding


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
            } else {
                Log.i("Permission: ", "Denied")
            }
        }
    //gets the photo from the camera
    private fun getPhotoFile(fileName: String): File {
        val storageDirectory = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        layout = binding.mainScreen

        //Set up the toolbar at the top of the screen
        setSupportActionBar(toolbar)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        setToolbarTitle("Freezer One")
        val user = auth.currentUser
        if (user != null) {
            userID=user.uid
        }
        //Check if a user is signed in if not send to sign in page else load home fragment
        if (user != null) {
            changeFragment(FreezerOne())
        } else {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
        //State Listener to Send back to sign in page when user signs out
        auth.addAuthStateListener {
            Log.i("firebase", "AuthState changed to ${it.currentUser?.uid}")
            if (it.currentUser != null) {
            } else {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }

            //menu settings
            val toggle =
                ActionBarDrawerToggle(this, main_screen, toolbar, R.string.Open, R.string.Close)
            toggle.isDrawerIndicatorEnabled = true
            main_screen.addDrawerListener(toggle)
            toggle.syncState()


            navigation.setNavigationItemSelectedListener(this)


        }


    }

    //Menu Reactions
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        main_screen.closeDrawer(GravityCompat.START)

        when (item.itemId) {
            R.id.home -> {
                setToolbarTitle("Freezer One")
                changeFragment(FreezerOne())
            }
            R.id.profile -> {
                setToolbarTitle("Profile")
                changeFragment(Profile())
            }

            R.id.addItem -> {
                setToolbarTitle("Add Item")
                changeFragment(AddItemToFreezer())
            }
            R.id.freezerTwo -> {
                setToolbarTitle("Freezer Two")
                changeFragment(FreezerTwo())
            }

        }


        return true
    }

    //Setting Toolbar Titles - Called when menu choice made
    private fun setToolbarTitle(title: String) {
        toolbar?.title = title
    }

    //Change Fragments - Called when Menu clicked
    private fun changeFragment(frag: Fragment) {
        val fragment = supportFragmentManager.beginTransaction()
        fragment.replace(R.id.fragment_container, frag).commit()
    }

    //Listen for Activity Result for Camera
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){

            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)

            takenImageDisplay.setImageBitmap(takenImage)
        }else{super.onActivityResult(requestCode, resultCode, data)}}






    //Show the Details Dialog
    fun showDialog() {
        val dialog = DetailsFragment()
        dialog.show(supportFragmentManager, "Details")
    }

    fun onClickRequestPermission(view: View) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                layout.showSnackbar(
                    view,
                    getString(R.string.permission_granted),
                    Snackbar.LENGTH_SHORT,
                    null
                ) {
                }
                takePicture() }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> {
                layout.showSnackbar(
                    view,
                    getString(R.string.permission_required),
                    Snackbar.LENGTH_INDEFINITE,
                    getString(R.string.ok)
                ) {
                    requestPermissionLauncher.launch(
                        Manifest.permission.CAMERA

                    )
                }
            }

            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA
                )
            }
        }
    }
fun takePicture(){
    //Delcare intent to take photo from built in camera app
    val takepictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    photoFile = getPhotoFile(FILE_NAME)
    //Set up a file provider to allow the camera to communicate with the app
    val fileProvider = FileProvider.getUriForFile(
        this,
        "com.example.freezergeez.fileprovider",
        photoFile
    )
    photoURI = Uri.parse(photoFile.path)

    //Take the picture if a camera app is available otherwise send a message
    takepictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
    if (takepictureIntent.resolveActivity(this.packageManager!!) != null) {

        startActivityForResult(takepictureIntent, REQUEST_CODE)
    } else {
        Toast.makeText(this, "No camera Application Found", Toast.LENGTH_SHORT).show()
    }}


}


fun View.showSnackbar(
    view: View,
    msg: String,
    length: Int,
    actionMessage: CharSequence?,
    action: (View) -> Unit
) {
    val snackbar = Snackbar.make(view, msg, length)
    if (actionMessage != null) {
        snackbar.setAction(actionMessage) {
            action(this)
        }.show()
    } else {
        snackbar.show()
    }
}




