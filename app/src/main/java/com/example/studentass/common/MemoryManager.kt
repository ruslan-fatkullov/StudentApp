package com.example.studentass.common

import android.content.Context
import com.example.studentass.models.Tokens

class MemoryManager {
    companion object {
        fun saveTokens(context: Context, token: String) {
            val sharedPreferences = context.getSharedPreferences("Tokens", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.apply {
                putString("accessToken", token)
            }.apply()
        }

        fun loadTokens(context: Context): String? {
            val sharedPreferences = context.getSharedPreferences("Tokens", Context.MODE_PRIVATE)
            val accessToken: String?
            val refreshToken: String?
            sharedPreferences.apply {
                accessToken = getString("accessToken", null)
            }
            return if (accessToken != null) {
                accessToken
            } else {
                null
            }
        }

        fun deleteTokens(context: Context) {
            val sharedPreferences = context.getSharedPreferences("Tokens", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear().apply()
        }
    }
}