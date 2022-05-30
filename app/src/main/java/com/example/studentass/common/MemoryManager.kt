package com.example.studentass.common

import android.content.Context

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
            sharedPreferences.apply {
                accessToken = getString("accessToken", null)
            }
            return accessToken
        }

        fun deleteTokens(context: Context) {
            val sharedPreferences = context.getSharedPreferences("Tokens", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear().apply()
        }

        fun saveColorTheme(themeNumber: String, context: Context) {
            val sharedPreferences = context.getSharedPreferences("Theme", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.apply {
                putString("themeNumber", themeNumber)
            }.apply()
        }

        fun loadColorTheme(context: Context): String? {
            val sharedPreferences = context.getSharedPreferences("Theme", Context.MODE_PRIVATE)
            val colorTheme: String?
            sharedPreferences.apply {
                colorTheme = getString("themeNumber", null)
            }
            return colorTheme
        }

        fun deleteColorTheme(context: Context) {
            val sharedPreferences = context.getSharedPreferences("Theme", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear().apply()
        }

    }
}