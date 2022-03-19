package com.seaid.hivetforvet

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.seaid.hivetforvet.models.Saldo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class TarikSaldoFragment : Fragment() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fgv1 = inflater.inflate(R.layout.fragment_tarik_saldo, container, false)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val button = fgv1.findViewById<Button>(R.id.add)
        val nama = fgv1.findViewById<EditText>(R.id.namaTV)
        val tarik = fgv1.findViewById<EditText>(R.id.tarikTV)
        val jml = fgv1.findViewById<EditText>(R.id.jumlahtv)
        val ke = fgv1.findViewById<EditText>(R.id.norektv)

        button.setOnClickListener {
            val length = 8
            val id : String = getRandomString(length)
            val current = LocalDateTime.now()
            val simpleDateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
            val tanggal = current.format(simpleDateFormat)
            val tariksaldo = Saldo(id, mAuth.currentUser!!.uid,
                jml.text.toString(), tarik.text.toString(), nama.text.toString(), ke.text.toString(), tanggal, "Proses"
            )
            db = FirebaseFirestore.getInstance()
            db.collection("saldo").document(id).set(tariksaldo)
                .addOnSuccessListener {
                    Toast.makeText(fgv1.context, "Berhasil mengajukan penarikan", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(fgv1.context, "Error "+it.message, Toast.LENGTH_SHORT).show()
                }
        }
        return fgv1
    }

    fun getRandomString(length: Int) : String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

}