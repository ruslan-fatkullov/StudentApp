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
import com.example.studentass.adapters.SubjectsRvAdapterNew
import com.example.studentass.fragments.LoginFragment.Companion.token
import com.example.studentass.getAppCompatActivity
import com.example.studentass.models.Subject
import com.example.studentass.services.SubjectApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_subjects.*
import kotlinx.android.synthetic.main.fragment_subjects.subjectAbsenceTv
import kotlinx.android.synthetic.main.fragment_subjects_new.*
import java.util.ArrayList


/*
 * Фрагмент со списком предметов
 */
class SubjectsFragmentNew : Fragment() {
    private val subjectApiService = SubjectApiService.create()
    private val compositeDisposable = CompositeDisposable()
    //private var subjects: List<Subject>? = null


    companion object{
        var curSub: Subject? = null
        var subjects: List<Subject>? = null
    }


    /*
     * Инициализация элементов интерфейса
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //subject_viewPager.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        //subject_viewPager.adapter = SubjectsRvAdapterNew(context!!)


        val requestBody = "Bearer " + token
        //val adapter = subject_viewPager.adapter as SubjectsRvAdapterNew
        val disposableSubjectListRx = subjectApiService
            .getIdSubject(requestBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {r -> onGetIdsSubject(r)},
                {e -> Toast.makeText(context, "Get subject list error: $e", Toast.LENGTH_LONG).show()}
            )
        compositeDisposable.add(disposableSubjectListRx)

//        adapter.setOnItemClickListener(object: SubjectsRvAdapterNew.onItemClickListener{
//            override fun setOnClickListener(position: Int) {
//
//            }
//
//        })
//
//        adapter.notifyDataSetChanged()


        onHiddenChanged(false)
    }


    /*
     * Наполнение страницы элемнтами интерфейса
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_subjects_new, container, false)
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
    private fun onGetIdsSubject(subjectList: List<Subject>) {
        if (subjectList.isEmpty()){
            subjectAbsenceTv.visibility = View.VISIBLE
        }else{
            subjects = subjectList

            curSub = subjectList[0]
            val subjectInfoFragmentNew = SubjectInfoFragmentNew()
            val sfm = getAppCompatActivity<MainActivity>()!!.fragmentManager
            sfm.beginTransaction().add(subject_frame_layout.id, subjectInfoFragmentNew).commit()
        }


//        adapter.dataList = subjects as ArrayList<Subject>
//        for (i in 0..adapter.dataList.size){
//            getAppCompatActivity<MainActivity>()?.createFMforSubject()?.let { SubjectsRvAdapterNew.subFragmentManager.add(it) }
//        }

    }







}








