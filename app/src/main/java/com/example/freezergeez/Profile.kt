package com.example.freezergeez

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.freezergeez.MainActivity.Companion.userID
import com.example.freezergeez.databinding.FragmentProfileBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth


class Profile : Fragment(R.layout.fragment_profile)  {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //Get the Google Information
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("your ID")
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(activity, gso)

        auth = FirebaseAuth.getInstance()



    }
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //binding allows easy communication between the xml and .kt files
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentUser = auth.currentUser
        binding.signOutButton.setOnClickListener{
            //Sign out of current session
            auth.signOut()
            // Google sign out -- if this is not done it will automatically log in
            googleSignInClient.signOut()


        }
        //Get the user information
        userID= currentUser!!.uid
        //Gets User name and Email
        binding.usernameTxt.text  = currentUser.displayName
        binding.emailTxt.text = currentUser.email
        //Display their user photo
        Glide.with(this).load(auth.currentUser?.photoUrl).override(500,500).into(binding.profileImage)


    }
    // To avoid memory leaks we set the binding back to null
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}
