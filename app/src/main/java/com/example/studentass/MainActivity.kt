package com.example.studentass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.os.Message
import android.view.View
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private var editTextEmail : EditText? = null
    private var editTextPassword : EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
    }

    fun onButtonLoginClick(view: View) {
        try {
            var email : String = editTextEmail?.text.toString()
            var password : String = editTextPassword?.text.toString()

            if (email.length < 1)
                throw Exception("Не указан адрес эл. почты")
            if (email.indexOf(' ', 0, true) != -1)
                throw Exception("Адрес эл. почты содержит недопустимые символы")
            if (password.length < 1)
                throw Exception("Не указан пароль")

            var message : String = "Почта: " + email + "    Пароль: " + password
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
        catch (e: Exception) {
            var errorMessage : String = "Ошибка: " + e.message
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }


}