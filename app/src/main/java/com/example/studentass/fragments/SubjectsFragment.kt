package com.example.studentass.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.studentass.AuthActivity
import com.example.studentass.R
import com.example.studentass.models.Schedule
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_subjects.*
import kotlin.concurrent.thread


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SubjectsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubjectsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subjects, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получение расписания из сервиса
        thread {
            var text : String
            try {
                val scheduleJsonString = AuthActivity.sendGet("https://my-json-server.typicode.com/AntonScript/schedule-service/GroupStudent")
                val scheduleObject = GsonBuilder().create().fromJson(scheduleJsonString, Schedule::class.java)
                //schedule = scheduleObject
                text = GsonBuilder().create().toJson(scheduleObject)
            } catch (e : Exception) {
                //Toast.makeText(context, "Schedule init error: $e", Toast.LENGTH_LONG).show()
                text = e.toString()
            }
            AuthActivity.mHandler.post {
                subjectsTestTV?.text = text
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SubjectsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SubjectsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}