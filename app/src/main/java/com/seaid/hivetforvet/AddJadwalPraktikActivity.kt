package com.seaid.hivetforvet

import android.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
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

        abinding.durationTV.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length != 0){
                    time()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //time()
            }

        })

        abinding.add.setOnClickListener {
            saveData()
        }


        showData()

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun time(){
        val start = abinding.timestartTV.text
        val end = abinding.timeendtTV.text
        val duration = abinding.durationTV.text
        val akhir = Integer.parseInt(duration.toString())

        val df = SimpleDateFormat("HH:mm")
        val d: Date = df.parse(start.toString())
        val s: Date = df.parse(end.toString())
        val cal: Calendar = Calendar.getInstance()
        var x = d
        var slot : Int = 0

        while (x.before(s)) {
            cal.setTime(x)
            cal.add(Calendar.MINUTE, akhir)
            val y = df.format(cal.getTime())
            x = df.parse(y)
            slot += 1
        }

        abinding.slottv.setText(slot.toString())
    }

    private fun EventChangeListener() {

        val data = FirebaseDatabase.getInstance().getReference("booking_appointments")
        data.orderByChild("tanggal").limitToLast(100)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snapshot in snapshot.children) {
                        val item : JanjiTemu? = snapshot.getValue(JanjiTemu::class.java)
                        janjiTemuList.add(item!!)
                    }
                    abinding.recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    throw error.toException()
                }
            })
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
                        abinding.jampraktikTV.setText(data!!.tanggal)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    throw error.toException()
                }

            })

        val uidRef = FirebaseDatabase.getInstance().getReference("drh")
        uidRef.child(mAuth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(Vet::class.java)
                    abinding.usernametv.text = user!!.Name
                    abinding.tempatpraktikTV.text = user!!.tempat
                    abinding.slottv.setText(user!!.status)
                    abinding.daerah.setText(user!!.alamat)
                }

                override fun onCancelled(error: DatabaseError) {
                    throw error.toException()
                }
            })
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
        hashMap["tanggal"] = date
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

        val hm = HashMap<String, Any>()
        hm["booking"] = abinding.daerah.text.toString() + "_" + date
        hm["status"] = "1"
        val updatedrh = FirebaseDatabase.getInstance().getReference("drh")
        updatedrh.child(mAuth.currentUser!!.uid).updateChildren(hm)
            .addOnFailureListener {
                throw it
            }
    }
}