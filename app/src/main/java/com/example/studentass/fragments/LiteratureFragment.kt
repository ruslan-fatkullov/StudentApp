package com.example.studentass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentass.R
import com.example.studentass.adapters.LiteratureRvAdapter
import com.example.studentass.models.*
import com.example.studentass.services.LiteratureApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_literature.*
import java.util.ArrayList

class LiteratureFragment : Fragment() {
    private val compositeDisposable = CompositeDisposable()
    private var literatureList: List<LiteratureData>? = null
    private val literatureApiService = LiteratureApiService.create()
    var currentLiterId: Long? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        literatureRv.layoutManager =
            LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        literatureRv.adapter = LiteratureRvAdapter(context!!)

        val adapter = literatureRv.adapter as LiteratureRvAdapter

        val indexOfSubject = SubjectsFragmentNew.subjects?.indexOf(SubjectsFragmentNew.curSub)
        try {
            val literatureList = SubjectInfoFragmentNew.listOfListLiterature[indexOfSubject!!]
            setFragParameters(adapter, literatureList)
        } catch (e: IndexOutOfBoundsException) {
            loadLiterature(adapter, indexOfSubject)
        }


        //loadLiterature(adapter)
        onHiddenChanged(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_literature, container, false)
    }


    private fun loadLiterature(adapter: LiteratureRvAdapter, indexOfSubject: Int?) {

        val requestBody = "Bearer " + LoginFragment.token
        val subjectId = SubjectsFragmentNew.curSub?.id
        val userId = 2
        val disposableLiteratureListRx = literatureApiService
            .getIdLiterature(requestBody, subjectId, userId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { r -> onGetIdsLiterature(r, adapter, indexOfSubject) },
                { e ->
                    Toast.makeText(context, "Get literature list error: $e", Toast.LENGTH_LONG)
                        .show()
                }
            )
        compositeDisposable.add(disposableLiteratureListRx)

    }

    private fun onGetIdsLiterature(
        literList: List<LiteratureData>,
        adapter: LiteratureRvAdapter,
        indexOfSubject: Int?,
        //indexOfSubject: Int?
    ) {
        literatureList = literList

        if (indexOfSubject != null) {
            SubjectInfoFragmentNew.listOfListLiterature.add(literList)
        }
        setFragParameters(adapter, literList)

    }

    private fun setFragParameters(
        adapter: LiteratureRvAdapter,
        literatureLis: List<LiteratureData>
    ) {
        adapter.dataList = literatureLis as ArrayList<LiteratureData>

        adapter.setOnItemClickListener(object : LiteratureRvAdapter.OnItemClickListener {
            override fun setOnClickListener(position: Int) {
                currentLiterId = literatureLis[position].id
                Toast.makeText(context, currentLiterId.toString(), Toast.LENGTH_SHORT).show()
            }

        })

        adapter.notifyDataSetChanged()

        if (literatureLis.isEmpty()) {
            contentAbsenceTv.visibility = View.VISIBLE
            contentAbsenceTv.text = "Литературы нет"
        }
    }


}


