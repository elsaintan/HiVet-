package com.seaid.hivetforvet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.seaid.hivetforvet.databinding.ActivityUserProfileBinding

class UserProfileActivity : AppCompatActivity() {


        //view binding
        private lateinit var binding: ActivityUserProfileBinding

        //firebase auth
        private lateinit var mAuth: FirebaseAuth

        private lateinit var mDbRef: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityUserProfileBinding.inflate(layoutInflater)
            val view = binding.root
            setContentView(view)

            //init firebasee auth
            mAuth = FirebaseAuth.getInstance()
            mDbRef = FirebaseFirestore.getInstance()

            val currentUser = mAuth.currentUser

            loadProfile(currentUser?.uid!!)

        }

        private fun loadProfile(id : String) {
            val uidRef  = mDbRef.collection("drh").document(id)

            uidRef.get().addOnSuccessListener { doc ->
                if (doc != null) {
                    val user = doc.toObject(Vet::class.java)
                    binding.namauTV.text = user!!.Name
                    binding.emailTV.text = user!!.email
                    if (user!!.photoProfile == ""){
                        binding.profileIM.setImageResource(R.drawable.profile)
                    }else{
                        Glide.with(this).load(user!!.photoProfile).into(binding.profileIM)
                    }
                    Toast.makeText(this, "{$user.name}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "No such document", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(this, "get failed with "+exception, Toast.LENGTH_SHORT).show()
            }



        }


    }