package com.seaid.hivetforvet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var eEmail: EditText
    private lateinit var ePassword: EditText
    private lateinit var bLogin: Button
    private lateinit var tvFPassword : TextView
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        eEmail = findViewById(R.id.emialET)
        ePassword = findViewById(R.id.passwordET)
        bLogin = findViewById(R.id.loginBt)
        tvFPassword = findViewById(R.id.fpasswordTv)
        tvFPassword = findViewById(R.id.fpasswordTv)

        mAuth = FirebaseAuth.getInstance()

        // handle click begin login
        bLogin.setOnClickListener {
            //before login validate data
            validateData()

        }


    }

    private fun forgotPassword() {
        //code for jumping to signup
        tvFPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    private fun validateData() {
        //get data
        val email = eEmail.text.toString()
        val password = ePassword.text.toString()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email format
            Toast.makeText(this, "Invalid E-mail Format", Toast.LENGTH_SHORT).show()
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
        }else{
            login(email, password)
        }


    }

    private fun login(email: String, password: String) {
//        logic for login user
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //login success
                    //get logged In User
                    val firebaseUser = mAuth.currentUser

                    //get user info
                    val uid = firebaseUser!!.uid

                    //jumping to Home
                    toHome(uid)
                } else {
                    Toast.makeText(this, "User doesn't exist", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e->
                //login failed
                Toast.makeText( this, "Login failed due to " +e.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun toHome(uid : String){
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra("Uid", uid)
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()
        //if user already logged in, go to Main Activity
        //get current user
        val currentUser = mAuth.currentUser
        if (currentUser != null){
            //user is already logged in
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }


}