package com.seaid.hivetforvet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.seaid.hivetforvet.databinding.ActivityAddJadwalPraktikBinding
import com.seaid.hivetforvet.models.Vet

class AddJadwalPraktikActivity : AppCompatActivity() {

    private lateinit var abinding : ActivityAddJadwalPraktikBinding
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDbRef : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        abinding = ActivityAddJadwalPraktikBinding.inflate(layoutInflater)
        val view = abinding.root
        setContentView(view)

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseFirestore.getInstance()

        abinding.add.setOnClickListener {
            saveData()
        }

        showData()

    }

    private fun showData() {
        val uidRef  = mDbRef.collection("drh").document(mAuth.currentUser!!.uid)

        uidRef.get().addOnSuccessListener { doc ->
            if (doc != null) {
                val user = doc.toObject(Vet::class.java)
                abinding.usernametv.text = user!!.Name
                abinding.tempatpraktikTV.text = user!!.tempat
                //abinding.jampraktikTV.text = user!!.
                //Toast.makeText(this, "{$user.name}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No such document", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "get failed with "+exception, Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveData() {

    }
}