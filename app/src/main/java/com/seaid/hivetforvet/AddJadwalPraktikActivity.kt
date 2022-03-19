package com.seaid.hivetforvet

import android.R
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.seaid.hivetforvet.adapters.JanjitemuAdapter
import com.seaid.hivetforvet.adapters.kBerjalanAdapter
import com.seaid.hivetforvet.databinding.ActivityAddJadwalPraktikBinding
import com.seaid.hivetforvet.models.JanjiTemu
import com.seaid.hivetforvet.models.Vet
import com.seaid.hivetforvet.models.konsultasi
import com.seaid.hivetforvet.utils.SpacingItemDecorator
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddJadwalPraktikActivity : AppCompatActivity() {

    private lateinit var abinding : ActivityAddJadwalPraktikBinding
    private lateinit var mAuth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var janjiTemuList: ArrayList<JanjiTemu>
    private lateinit var adapter: JanjitemuAdapter
    private var formatDate = SimpleDateFormat("dd MMMM yyyy", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        abinding = ActivityAddJadwalPraktikBinding.inflate(layoutInflater)
        val view = abinding.root
        setContentView(view)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        abinding.recyclerView.layoutManager = LinearLayoutManager(this)
        abinding.recyclerView.setHasFixedSize(false)
        janjiTemuList = arrayListOf()
        adapter = JanjitemuAdapter(janjiTemuList)
        EventChangeListener()
        abinding.recyclerView.addItemDecoration(SpacingItemDecorator(16))

        abinding.jampraktikTV.setOnClickListener {
            val getData : Calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(this, R.style.Theme_Holo_Light_Dialog_MinWidth,
                DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
                    val selectDate : Calendar = Calendar.getInstance()
                    selectDate.set(Calendar.YEAR, i)
                    selectDate.set(Calendar.MONTH, i2)
                    selectDate.set(Calendar.DAY_OF_MONTH, i3)
                    val date = formatDate.format(selectDate.time)
                    abinding.jampraktikTV.setText(date)
                    //Toast.makeText(this, "Date "+date, Toast.LENGTH_SHORT).show()
                }, getData.get(Calendar.YEAR), getData.get(Calendar.MONTH), getData.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }

        abinding.add.setOnClickListener {
            saveData()
        }

        showData()

    }

    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("booking_appointments")
            .get()
            .addOnSuccessListener {
                val data = it.toObjects(JanjiTemu::class.java)
                val items = data.size
                if (items > 0){
                    for (item in data){
                        if(item.drh_id == mAuth.uid){
                            janjiTemuList.add(item)
                        }
                    }
                    abinding.recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }
    }

    private fun showData() {
        val uidRef  = db.collection("drh").document(mAuth.currentUser!!.uid)

        uidRef.get().addOnSuccessListener { doc ->
            if (doc != null) {
                val user = doc.toObject(Vet::class.java)
                abinding.usernametv.text = user!!.Name
                abinding.tempatpraktikTV.text = user!!.tempat
                abinding.jampraktikTV.setText(user!!.booking)
                abinding.slottv.setText(user!!.status)
                //Toast.makeText(this, "{$user.name}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No such document", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "get failed with "+exception, Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveData() {
        val date = abinding.jampraktikTV.text
        val slot = abinding.slottv.text
       db.collection("drh").document(mAuth.currentUser!!.uid)
        .update("status", slot.toString(),
        "booking", date.toString())
            .addOnSuccessListener {
                Toast.makeText(this, "Berhasil Mengubah Jadwal", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                throw it
            }
    }
}