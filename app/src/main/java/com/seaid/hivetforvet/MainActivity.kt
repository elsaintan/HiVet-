package com.seaid.hivetforvet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.seaid.hivetforvet.databinding.ActivityMainBinding
import com.seaid.hivetforvet.databinding.ActivityUserProfileBinding
import com.seaid.hivetforvet.models.drh

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding : ActivityMainBinding

    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDbRef : FirebaseFirestore

    companion object {
        private const val CAMERA_PERMISSION_CODE = 100
        private const val STORAGE_PERMISSION_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseFirestore.getInstance()

        val uId = mAuth.currentUser!!.uid

        setUser(uId)
    }

    private fun setUser(uId: String) {
        val uidRef  = mDbRef.collection("drh").document(uId)

        uidRef.get().addOnSuccessListener { doc ->
            if (doc != null) {
                val user = doc.toObject(drh::class.java)
                mBinding.userName.text = "Hai "+user!!.Name+"!"
                if (user!!.photoProfile == "" || user.photoProfile == null){
                    mBinding.imageView3.setImageResource(R.drawable.profile)
                }else{
                    Glide.with(this).load(user!!.photoProfile).into(mBinding.imageView3)
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