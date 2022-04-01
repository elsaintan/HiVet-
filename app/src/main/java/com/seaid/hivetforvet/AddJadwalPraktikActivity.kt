package com.seaid.hivetforvet

import android.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.*
import com.seaid.hivetforvet.adapters.JanjitemuAdapter
import com.seaid.hivetforvet.adapters.MessageAdapter
import com.seaid.hivetforvet.adapters.kBerjalanAdapter
import com.seaid.hivetforvet.databinding.ActivityAddJadwalPraktikBinding
import com.seaid.hivetforvet.models.*
import com.seaid.hivetforvet.utils.SpacingItemDecorator
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddJadwalPraktikActivity : AppCompatActivity() {

    private lateinit var abinding : ActivityAddJadwalPraktikBinding
    private lateinit var mAuth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private var reference: DatabaseReference? = null
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
        abinding.recyclerView.addItemDecoration(SpacingItemDecorator(120))

// this might be helpful


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

        abinding.timestartTV.setOnClickListener{
            val currentTime = Calendar.getInstance()
            val startHour = currentTime.get(Calendar.HOUR_OF_DAY)
            val startMinute = currentTime.get(Calendar.MINUTE)

            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener{ view, hourofDay, minute ->
                abinding.timestartTV.setText("$hourofDay:$minute")
            }, startHour, startMinute, true).show()
        }

        abinding.timeendtTV.setOnClickListener {
            val currentTime = Calendar.getInstance()
            val startHour = currentTime.get(Calendar.HOUR_OF_DAY)
            val startMinute = currentTime.get(Calendar.MINUTE)

            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener{ view, hourofDay, minute ->
                abinding.timeendtTV.setText("$hourofDay:$minute")
            }, startHour, startMinute, true).show()
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

        val reference = FirebaseDatabase.getInstance().getReference()
        reference.child("janjiTemu").child(mAuth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data= snapshot.getValue(jadwal::class.java)
                    if (data != null){
                        abinding.timestartTV.setText(data!!.start)
                        abinding.timeendtTV.setText(data!!.end)
                        abinding.durationTV.setText(data!!.duration)
                        abinding.slottv.setText(data!!.slot)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    throw error.toException()
                }

            })

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
        val id : String = mAuth.currentUser?.uid.toString()
        val date : String = abinding.jampraktikTV.text.toString()
        val slot : String = abinding.slottv.text.toString()
        val start : String = abinding.timestartTV.text.toString()
        val end : String = abinding.timeendtTV.text.toString()
        val duration : String = abinding.durationTV.text.toString()

        val reference = FirebaseDatabase.getInstance().getReference()
        val hashMap = HashMap<String, Any>()
        hashMap["id"] = id
        hashMap["start"] = start
        hashMap["end"] = end
        hashMap["duration"] = duration
        hashMap["slot"] = slot

        reference.child("janjiTemu").child(mAuth.currentUser!!.uid).setValue(hashMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Berhasil Mengubah Jadwal", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, " "+it, Toast.LENGTH_SHORT).show()
            }

       db.collection("drh").document(mAuth.currentUser!!.uid)
        .update("booking", date.toString(),
        "status", "1")
            .addOnSuccessListener {
                Toast.makeText(this, "Berhasil Mengubah Jadwal", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                throw it
            }
    }
}