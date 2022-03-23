package com.example.studentass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentass.MainActivity
import com.example.studentass.R
import com.example.studentass.adapters.NotificationsRvAdapter
import com.example.studentass.adapters.SchedulePairsRvAdapter
import com.example.studentass.getAppCompatActivity
import com.example.studentass.models.NotificationsItem
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.fragment_schedule.*


/*
 * Фрагмент уведомлений
 */
class NotificationsFragment : Fragment() {


    /*
     * Инициализация элементов интерфейса
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationsRv.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        notificationsRv.adapter = NotificationsRvAdapter(context!!)

        /*
         * Наполнение страницы фейковыми данными
         */
        val data = arrayListOf<NotificationsItem>()
        data.add(NotificationsItem(1,1,"Изменения в расписании", "Вы отчислены и можете не идти на пары"))
        data.add(NotificationsItem(1, 0,"Сообщение от Беляева И.В:", "Тебя ешё не отчислили?"))
        data.add(NotificationsItem(0, 0,"Сообщение от Беляева И.В:", "Тебя ешё не отчислили?"))

        val adapter = notificationsRv.adapter as NotificationsRvAdapter
        adapter.dataList = data
        adapter.notifyDataSetChanged()

        onHiddenChanged(false)
    }


    /*
     * Наполнение фрагмента
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }


    /*
     * Управление заголовком страницы
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden) {
            getAppCompatActivity<MainActivity>()?.actionBar?.title = "Уведомления"
        }
    }
}