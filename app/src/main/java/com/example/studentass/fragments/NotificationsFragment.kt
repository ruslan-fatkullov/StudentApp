package com.example.studentass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.studentass.MainActivity.Companion.mainActivity
import com.example.studentass.R

class NotificationsFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получение тестовых вопросов из сервиса
        /*thread {
            var text : String
            try {
                val questionsJsonString = AuthActivity.sendGet("https://my-json-server.typicode.com/fridayeveryday/testService/test")
                val questions = GsonBuilder().create().fromJson(questionsJsonString, Test::class.java)
                text = GsonBuilder().create().toJson(questions)
            } catch (e : Exception) {
                text = e.toString()
            }
            AuthActivity.mHandler.post {
                notificationsTestTV?.text = text
            }
        }*/
        onHiddenChanged(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden) {
            mainActivity.actionBar.title = "Уведомления"
        }
    }
}