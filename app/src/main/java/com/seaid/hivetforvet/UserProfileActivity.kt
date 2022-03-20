package com.seaid.hivetforvet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.seaid.hivetforvet.databinding.ActivityUserProfileBinding
import com.seaid.hivetforvet.models.Vet

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
            binding.suntingTV.setOnClickListener {
                startActivity(Intent(this, EditProfileActivity::class.java))
            }

        }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun loadProfile(id : String) {
            val uidRef  = mDbRef.collection("drh").document(id)

            uidRef.get().addOnSuccessListener { doc ->
                if (doc != null) {
                    val user = doc.toObject(Vet::class.java)
                    binding.userNameTV.text = user!!.Name
                    binding.kontakUserTV.text = user.Contact
                    binding.userPraktikTV.text = user.tempat
                    binding.userSTRtv.text = user.STR
                    binding.userSIPtv.text = user.SIP
                    binding.userWprkExpTV.text = user.WorkExp
                    if (user!!.photoProfile == "" || user.photoProfile == null){
                        //binding.profileIM.setImageResource(R.drawable.profile)
                    }else{
                        Glide.with(this).load(user!!.photoProfile).into(binding.profileIM)
                    }
                    //Toast.makeText(this, "{$user.name}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "No such document", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(this, "get failed with "+exception, Toast.LENGTH_SHORT).show()
            }



        }


    }