package com.example.studentass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.os.Message
import android.view.View
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private var editTextName : EditText? = null
    private var editTextPincode : EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextName = findViewById(R.id.editTextName)
        editTextPincode = findViewById(R.id.editTextPincode)
    }

    fun onButtonLoginClick(view: View) {
        var name : String = editTextName?.text.toString()
        var pincode : String = editTextPincode?.text.toString()

        var message : String = "Name: " + name + "   Pincode: " + pincode
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}