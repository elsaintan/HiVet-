package com.seaid.hivetforvet

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.seaid.hivetforvet.databinding.ActivityEditProfileBinding
import com.seaid.hivetforvet.databinding.ActivityUserProfileBinding
import com.seaid.hivetforvet.models.Vet
import java.io.IOException

class EditProfileActivity : AppCompatActivity() {

    private var filePath : Uri? = null
    private final val PICK_IMAGE_REQUEST : Int = 2020

    private lateinit var editBinding: ActivityEditProfileBinding

    private lateinit var mAuth: FirebaseUser
    private lateinit var mDbRef: FirebaseFirestore
    private lateinit var storage : FirebaseStorage
    private lateinit var storageRef : StorageReference

    private var photo : String ?= null
    var counter : Int = 0

    companion object {
        private const val CAMERA_PERMISSION_CODE = 100
        private const val STORAGE_PERMISSION_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editBinding = ActivityEditProfileBinding.inflate(layoutInflater)
        val view = editBinding.root
        setContentView(view)

        mAuth = FirebaseAuth.getInstance().currentUser!!
        mDbRef = FirebaseFirestore.getInstance()


        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        showDataUser()
        editBinding.saveBTN.visibility = View.VISIBLE

        editBinding.userImage.setOnClickListener{
            chooseImage()
        }

        editBinding.saveBTN.setOnClickListener {
            if (filePath.toString() == photo){
                updateDataUser()
            }else{
                uploadImage()
                updateDataUser()
            }
            editBinding.progressBar.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun chooseImage(){
        val intent: Intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST)
    }

    private fun uploadImage() {
        if(filePath != null){

            var ref:StorageReference = storageRef.child("Image/"+mAuth.uid)
            ref.putFile(filePath!!)
                .addOnSuccessListener {
                    editBinding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Uploaded", Toast.LENGTH_SHORT).show()
                    editBinding.saveBTN.visibility = View.GONE
                }
                .addOnFailureListener {
                    OnFailureListener{
                        editBinding.progressBar.visibility = View.GONE
                        Toast.makeText(this, "Failed "+it.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode != null){
            filePath = data!!.data
            try {
                var bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                editBinding.userImage.setImageBitmap(bitmap)

            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    private fun updateDataUser() {
        /*val name = editBinding.nameTV.text.toString()
        val email = editBinding.emailTV.text.toString()
        val useredit = Vet()

        val user = mDbRef.collection("users")
        user.document(mAuth.uid).set(useredit)
        startActivity(Intent(this, MainActivity::class.java))*/

        val name = editBinding.userNameTV.text
        val kontak = editBinding.kontakUserTV.text
        val tempat = editBinding.userPraktikTV.text
        val alanat = editBinding.userSTRtv.text

        mDbRef.collection("drh").document(mAuth!!.uid)
            .update("Name", name.toString(),
                "Contact", kontak.toString(),
            "tempat", tempat.toString(),
            "alamat", alanat.toString())
            .addOnSuccessListener {
                Toast.makeText(this, "Berhasil Mengubah Jadwal", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                throw it
            }

    }

    private fun showDataUser() {
        checkPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            EditProfileActivity.STORAGE_PERMISSION_CODE
        )
        val uidRef  = mDbRef.collection("drh").document(mAuth.uid)
        uidRef.get().addOnSuccessListener { doc ->
            if (doc != null) {
                val user = doc.toObject(Vet::class.java)
                editBinding.userNameTV.setText(user!!.Name)
                editBinding.kontakUserTV.setText(user!!.Contact)
                editBinding.userPraktikTV.setText(user.tempat)
                editBinding.userdom.setText(user.alamat)
                editBinding.userSTRtv.setText(user.STR)
                editBinding.userSIPtv.setText(user.SIP)
                editBinding.userWprkExpTV.setText(user.WorkExp)
                photo = user!!.photoProfile
                if (user!!.photoProfile == "" || user!!.photoProfile == null){
                    editBinding.userImage.setImageResource(R.drawable.profile)
                }else{
                    Glide.with(this).load(user!!.photoProfile).into(editBinding.userImage)
                }
                    //Toast.makeText(this, "{$user.name}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No such document ", Toast.LENGTH_SHORT).show()
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
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}