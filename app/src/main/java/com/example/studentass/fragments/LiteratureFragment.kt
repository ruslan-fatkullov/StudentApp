package com.example.studentass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentass.MainActivity
import com.example.studentass.R
import com.example.studentass.adapters.LiteratureRvAdapter
import com.example.studentass.adapters.TaskRvAdapter
import com.example.studentass.adapters.TestRvAdapter
import com.example.studentass.getAppCompatActivity
import com.example.studentass.models.*
import com.example.studentass.services.LiteratureApiService
import com.example.studentass.services.SubjectApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_literature.*
import kotlinx.android.synthetic.main.fragment_schedule.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.ArrayList

class LiteratureFragment : Fragment() {
    private val compositeDisposable = CompositeDisposable()
    private var literatureList: List<LiteratureData>? = null
    private val literatureApiService = LiteratureApiService.create()
    public var currentLiterId: Long? = null



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        loadLiterarute()
        onHiddenChanged(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_literature, container, false)
    }


    private fun onGetIdsLiterature(
        literatureLis: List<LiteratureData>,
        adapter: LiteratureRvAdapter
    ) {
        literatureList = literatureLis
        adapter.dataList = literatureList as ArrayList<LiteratureData>

        adapter.setOnItemClickListener(object : LiteratureRvAdapter.onItemClickListener {
            override fun setOnClickListener(position: Int) {
                currentLiterId = (literatureList as ArrayList<LiteratureData>).get(position).id
                Toast.makeText(context, currentLiterId.toString(), Toast.LENGTH_SHORT).show()
            }

        })

        adapter.notifyDataSetChanged()

    }

    fun loadLiterarute() {
        literatureRv.layoutManager =
            LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        literatureRv.adapter = LiteratureRvAdapter(context!!)

        val requestBody = "Bearer " + LoginFragment.token
        val subjectId = SubjectsFragmentNew.curSub?.id?.toLong()
        val userId = 2
        val adapter = literatureRv.adapter as LiteratureRvAdapter
        val disposableLiteratureListRx = literatureApiService
            .getIdLiterature(requestBody, subjectId, userId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { r -> onGetIdsLiterature(r, adapter) },
                { e ->
                    Toast.makeText(context, "Get literature list error: $e", Toast.LENGTH_LONG)
                        .show()
                }
            )
        compositeDisposable.add(disposableLiteratureListRx)

    }




}


