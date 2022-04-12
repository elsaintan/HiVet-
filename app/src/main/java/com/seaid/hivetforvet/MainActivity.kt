package com.seaid.hivetforvet

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.seaid.hivetforvet.adapters.kBerjalanAdapter
import com.seaid.hivetforvet.databinding.ActivityMainBinding
import com.seaid.hivetforvet.models.Saldo
import com.seaid.hivetforvet.models.Vet
import com.seaid.hivetforvet.models.konsultasi

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding : ActivityMainBinding

    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDbRef : FirebaseFirestore
    private lateinit var reference: DatabaseReference
    private var backPressedTime = 0L

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
        hitungSaldo()

        mBinding.imageLogout.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        mBinding.imageProfile.setOnClickListener {
            startActivity(Intent(this, UserProfileActivity::class.java))
        }

        mBinding.bkonsultasi.setOnClickListener {
            startActivity(Intent(this, KonsultasiActivity::class.java))
        }

        mBinding.bBooking.setOnClickListener {
            startActivity(Intent(this, AddJadwalPraktikActivity::class.java))
        }

        mBinding.imageSetting.setOnClickListener{
            startActivity(Intent(this, RiwayatKonsulActivity::class.java))
            finish()
        }

        mBinding.imageHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        mBinding.tariksaldo.setOnClickListener {
            startActivity(Intent(this, SaldoActivity::class.java))
        }

        listKonsul()
    }

    private fun listKonsul() {
        val reference = FirebaseDatabase.getInstance().getReference("konsultasi")
        var jml = 0

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snapshot in snapshot.children) {
                    val data: konsultasi? = snapshot.getValue(konsultasi::class.java)
                    if (data?.status!!.equals("1")) {
                        jml += 1
                    }
                }
                mBinding.jmlKonsul.setText(jml.toString())

            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException();
            }
        })
    }

    override fun onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed()
        }else{
            Toast.makeText(this, "Press back again to exit app", Toast.LENGTH_SHORT).show()
        }

        backPressedTime = System.currentTimeMillis()
    }

    private fun hitungSaldo() {
        var saldo = 0
        mDbRef = FirebaseFirestore.getInstance()
        mDbRef.collection("saldo")
            .get()
            .addOnSuccessListener {
                val data = it.toObjects(Saldo::class.java)
                val items = data.size
                if (items > 0) {
                    for (item in data) {
                        if (item.id_drh == mAuth.currentUser!!.uid) {
                            saldo += item.jumlah!!.toDouble().toInt()
                        }
                    }
                }
                mBinding.labelSaldo.text = "Rp $saldo"
            }
    }

    private fun setUser(uId: String) {
        val uidRef  = mDbRef.collection("drh").document(uId)

        uidRef.get().addOnSuccessListener { doc ->
            if (doc != null) {
                val user = doc.toObject(Vet::class.java)
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

    // Function to check and request permission.
    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        } else {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show()
        }
    }

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MainActivity.CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == MainActivity.STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}