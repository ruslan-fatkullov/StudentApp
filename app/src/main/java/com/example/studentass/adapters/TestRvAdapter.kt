package com.example.studentass.adapters

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.studentass.R
import com.example.studentass.models.PassedTests
import com.example.studentass.models.TestThemesData
import kotlinx.android.synthetic.main.fragment_test.view.*
import kotlinx.android.synthetic.main.test_layout_item.view.*

class TestRvAdapter(private val context: Context) : RecyclerView.Adapter<TestRvAdapter.ViewHolder>() {


    private lateinit var mListner: onItemClickListener
    class ViewHolder(view: View, var mListner: onItemClickListener): RecyclerView.ViewHolder(view), View.OnClickListener {

        val test_item = view.test_item
        val nameOfTestV = view.nameOfTestV
        val descriptionOfTest = view.descriptionOfTest
        val button2 = view.button2
        val numberOfTest = view.numberOfTest

        val countOfTryes = view.countOfTryes
        val resultOfTest = view.resultOfTest

        //val typeOfTask = when(itemView.)


        fun bind(itemData: PassedTests, context: Context, position: Int) {
            var drawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.select_answer_item_background)!!)
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP);
            test_item.background = drawable

            nameOfTestV.text = itemData.theme.name
            descriptionOfTest.text = itemData.theme.decryption
            numberOfTest.text = "#${(position+1).toString()}"


            val countTry = "Попыток: ${itemData.ratings.size}"
            val maxResult = "Результат: ${itemData.ratings.maxOrNull()}%"
            countOfTryes.text = countTry
            resultOfTest.text = maxResult

            var drawable1 = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.button_green_background)!!)
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP);
            button2.background = drawable1
        }

        init {
            button2.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (mListner != null){
                mListner.setOnClickListener(adapterPosition)
            }
        }
    }

    var dataList = ArrayList<PassedTests>()
    //var passedTests = ArrayList<PassedTests>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TestRvAdapter.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return TestRvAdapter.ViewHolder(inflater.inflate(R.layout.test_layout_item, parent, false), mListner)
    }




    override fun onBindViewHolder(holder: TestRvAdapter.ViewHolder, position: Int) {
        val itemData = dataList[position]
        holder.bind(itemData, context, position)
        holder.button2.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                if (mListner != null){
                    mListner.setOnClickListener(holder.getAdapterPosition())
                }
            }
        })


    }



    override fun getItemCount(): Int = dataList.size
    interface onItemClickListener{
        fun setOnClickListener(position: Int)
    }
    fun setOnItemClickListener(mListner: onItemClickListener) {
        this.mListner = mListner
    }

}