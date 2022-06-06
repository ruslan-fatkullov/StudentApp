package com.example.studentass.fragments

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentass.MainActivity
import com.example.studentass.R
import com.example.studentass.adapters.ChatRvAdapter
import com.example.studentass.getAppCompatActivity
import com.example.studentass.models.WorkModel
import kotlinx.android.synthetic.main.fragment_chat.*
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class ChatFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chat_layout.layoutManager =
            LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        chat_layout.adapter = ChatRvAdapter(context!!)
        val adapter = chat_layout.adapter as ChatRvAdapter


        val data = arrayListOf<WorkModel>()
        //val fileIds : Array = arrayListOf(1,2,4)
        data.add(
            WorkModel(
                1,
                1,
                1,
                null,
                213.123,
                1654358995.0,
                "Добрый день, да",
                "Здравствуйте, вы приняли мою работу?",
                "Five"
            )
        )
        data.add(
            WorkModel(
                1,
                1,
                1,
                null,
                213.123,
                3242342.2342342,
                "",
                "Хорошо, спасибо",
                "Five"
            )
        )
        adapter.dataList = data
        adapter.notifyDataSetChanged()



        autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                imageView13.visibility = View.VISIBLE
            }
        })
        imageView12.setOnClickListener {
            getAppCompatActivity<MainActivity>()?.switchDown()
        }
        imageView13.setOnClickListener{
            val newMess = autoCompleteTextView.text.toString()
            autoCompleteTextView.text.clear()
            imageView13.visibility = View.GONE

            val secondApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")

            //val f = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
            val currentTimestamp = 1654358995
            val timestampAsDateString = DateTimeFormatter.ISO_INSTANT
                .format(Instant.ofEpochSecond(currentTimestamp.toLong()))

            Log.d("parseTesting", timestampAsDateString) // prints 2019-08-07T20:27:45Z


            val date = LocalDate.parse(timestampAsDateString, secondApiFormat)

            Log.d("parseTesting", date.dayOfWeek.toString()) // prints Wednesday
            Log.d("parseTesting", date.month.toString()) // prints August

            adapter.dataList.add(
                WorkModel(
                    1,
                    1,
                    1,
                    null,
                    213.123,
                    3242342.2342342,
                    "",
                    newMess,
                    "Five"
                )
            )

            adapter.notifyDataSetChanged()
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

}