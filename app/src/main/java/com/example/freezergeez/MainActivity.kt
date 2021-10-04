package com.example.freezergeez

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_add_item_to_freezer.*
import kotlinx.android.synthetic.main.main_screen.*


class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
companion object{
   lateinit var userID:String
}
    private lateinit var auth: FirebaseAuth




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){

            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)

            takenImageDisplay.setImageBitmap(takenImage)
        }else{super.onActivityResult(requestCode, resultCode, data)}}

    fun showDialog() {
        val dialog = DetailsFragment()
        dialog.show(supportFragmentManager, "Details")
    }

}


