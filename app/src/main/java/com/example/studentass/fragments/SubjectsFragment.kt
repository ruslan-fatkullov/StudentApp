package com.example.studentass.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentass.MainActivity
import com.example.studentass.R
import com.example.studentass.adapters.SubjectsRvAdapter
import com.example.studentass.fragments.LoginFragment.Companion.token
import com.example.studentass.getAppCompatActivity
import com.example.studentass.models.Subject
import com.example.studentass.services.SubjectApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_subjects.*
import java.util.ArrayList


/*
 * Фрагмент со списком предметов
 */
class SubjectsFragment : Fragment() {
    private val subjectApiService = SubjectApiService.create()
    private val compositeDisposable = CompositeDisposable()
    private var subjects: List<Subject>? = null


    companion object{
        var curSub: Subject? = null
    }


    /*
     * Инициализация элементов интерфейса
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subjectsRv.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        subjectsRv.adapter = SubjectsRvAdapter(context!!)


        val requestBody = "Bearer " + token
//        val userId = 56
        val adapter = subjectsRv.adapter as SubjectsRvAdapter
        val disposableSubjectListRx = subjectApiService
            .getIdSubject(requestBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {r -> onGetIdsSubject(r, adapter)},
                {e -> Toast.makeText(context, "Get subject list error: $e", Toast.LENGTH_LONG).show()}
            )
        compositeDisposable.add(disposableSubjectListRx)

        adapter.setOnItemClickListener(object: SubjectsRvAdapter.onItemClickListener{
            override fun setOnClickListener(position: Int) {
                curSub = adapter.dataList[position]
                getAppCompatActivity<MainActivity>()?.actionBar?.title = curSub!!.name
                getAppCompatActivity<MainActivity>()?.switchUp(SubjectInfoFragment::class.java)
            }

        })

        adapter.notifyDataSetChanged()


        onHiddenChanged(false)
    }


    /*
     * Наполнение страницы элемнтами интерфейса
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_subjects, container, false)
    }


    /*
     * Управление заголовком страницы
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden) {
            getAppCompatActivity<MainActivity>()?.actionBar?.title = "Предметы"
        }
    }


    /*
    * Вызывается при успешном получении списка предметов
    */
    private fun onGetIdsSubject(subjectList: List<Subject>, adapter: SubjectsRvAdapter) {
        subjects = subjectList
        adapter.dataList = subjects as ArrayList<Subject>
    }

    private fun onGetAllSubject(subjectList: List<Subject>, adapter: SubjectsRvAdapter){
        subjects = subjectList
        adapter.dataList = subjects as ArrayList<Subject>
    }





}








