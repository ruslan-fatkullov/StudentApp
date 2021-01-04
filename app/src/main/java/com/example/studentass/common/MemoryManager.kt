package com.example.studentass.common

import android.content.Context
import com.example.studentass.models.Tokens

class MemoryManager {
    companion object {
        fun saveTokens(context: Context, tokens: Tokens) {
            val sharedPreferences = context.getSharedPreferences("Tokens", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.apply {
                putString("accessToken", tokens.accessToken)
                putString("refreshToken", tokens.refrashToken)
            }.apply()
        }

        fun loadTokens(context: Context): Tokens? {
            val sharedPreferences = context.getSharedPreferences("Tokens", Context.MODE_PRIVATE)
            val accessToken: String?
            val refreshToken: String?
            sharedPreferences.apply {
                accessToken = getString("accessToken", null)
                refreshToken = getString("refreshToken", null)
            }
            return if (accessToken != null && refreshToken != null) {
                Tokens(accessToken, refreshToken)
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