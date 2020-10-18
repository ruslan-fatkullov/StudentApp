package com.example.studentass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class MainActivity2 : AppCompatActivity() {
    companion object {
        const val MESSAGE = "message"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val message = intent.getStringExtra(MESSAGE)
        val textViewMessage : TextView = findViewById(R.id.textViewMessage)
        textViewMessage.text = message
    }

    fun onButtonBackClick(view: View) {
        val intentActivity = Intent(this, MainActivity::class.java)
        startActivity(intentActivity)
    }
}