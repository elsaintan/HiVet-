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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.seaid.hivetforvet.databinding.ActivityEditProfileBinding
import com.seaid.hivetforvet.databinding.ActivityUserProfileBinding
import com.seaid.hivetforvet.models.Vet
import java.io.IOException
import java.util.HashMap

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
            //chooseImage()
        }

        editBinding.saveBTN.setOnClickListener {
            if (filePath.toString() == photo){
                updateDataUser()
            }else{
                //uploadImage()
                updateDataUser()
            }
            //editBinding.progressBar.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    /*private fun chooseImage(){
        val intent: Intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST)
    }*/

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
    }*/

    private fun updateDataUser() {
        val name = editBinding.userNameTV.text.toString()
        val kontak = editBinding.kontakUserTV.text.toString()
        val tempat = editBinding.userdom.text.toString()
        val alanat = editBinding.userSTRtv.text.toString()



        Toast.makeText(this, name, Toast.LENGTH_SHORT).show()

        val ref = FirebaseDatabase.getInstance().getReference()
        val hm = HashMap<String, Any>()
        hm["Name"] = name
        hm["Contact"] = kontak
        hm["tempat"] = tempat
        hm["alamat"] = alanat

        ref.child("drh").child(mAuth.uid).updateChildren(hm)
            .addOnSuccessListener{
                Toast.makeText(this, "Berhasil Memperbarui Data", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                throw it
            }

    }

    private fun showDataUser() {

        val ref = FirebaseDatabase.getInstance().getReference("drh")
        ref.child(mAuth.uid)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user : Vet? = snapshot.getValue(Vet::class.java)
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
                        Glide.with(this@EditProfileActivity).load(user!!.photoProfile).into(editBinding.userImage)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@EditProfileActivity, "get failed with "+error, Toast.LENGTH_SHORT).show()
                }

            })
    }
}