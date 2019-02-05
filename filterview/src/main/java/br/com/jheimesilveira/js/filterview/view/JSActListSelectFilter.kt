package br.com.jheimesilveira.js.filterview.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import br.com.jheimesilveira.js.filterview.R
import br.com.jheimesilveira.js.filterview.adapter.AdapterListGroupItemSelected
import br.com.jheimesilveira.js.filterview.dto.JSGroupFilterModel
import br.com.jheimesilveira.js.filterview.dto.JSItemFilterModel
import br.com.jheimesilveira.js.filterview.dto.Params
import br.com.jheimesilveira.js.filterview.dto.ParamsSelectItems
import br.com.jheimesilveira.js.filterview.util.Util
import kotlinx.android.synthetic.main.js_act_list_select_filter.*

class JSActListSelectFilter : AppCompatActivity() {

    companion object {
        const val PARAMS = "PARAMS"
    }

    var params: Params? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.js_act_list_select_filter)
        initComponents()
    }

    private fun initComponents() {
        params = intent.getSerializableExtra(PARAMS) as Params
        initToolbar()
        initLlContainerBody()
        initBtnFinish()
    }

    private fun initLlContainerBody() {
        llContainerBody.removeAllViews()
        for (itemGroup in params?.listDataSetFilter!!.withIndex()) {
            when (itemGroup.value.type) {
                JSGroupFilterModel.Type.LINEAR -> {
                    generateViewLinear(itemGroup.value,
                        params!!.listDataSetFilterSelected[itemGroup.index], itemGroup.index)
                }
                JSGroupFilterModel.Type.GRID -> {
                    generateViewGrid(itemGroup.value,
                        params!!.listDataSetFilterSelected[itemGroup.index])
                }
                JSGroupFilterModel.Type.PROGRESS -> {
                    generateViewProgress(itemGroup.value,
                        params!!.listDataSetFilterSelected[itemGroup.index])
                }
            }
        }
    }

    //TODO implementar barra de progresso
    private fun generateViewProgress(
        itemGroup: JSGroupFilterModel,
        itemGroupSelected: JSGroupFilterModel) {

    }

    private fun generateViewGrid(
        itemGroup: JSGroupFilterModel,
        itemGroupSelected: JSGroupFilterModel) {

        val view = LayoutInflater.from(this@JSActListSelectFilter)
            .inflate(R.layout.js_item_group_selected_grid, null, false)

        (view.findViewById(R.id.tvTitleItemGroupSelectedGrid) as TextView).text = itemGroup.title

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvItemGroupSelectedGrid)
        val adapterListSelected = AdapterListGroupItemSelected(
            mContext = this@JSActListSelectFilter,
            dataSet = itemGroup.dataSet,
            selecteds = itemGroupSelected.dataSet,
            colorPrimary = params?.colorBackground!!,
            multiple = itemGroup.multiple
        )

        recyclerView.adapter = adapterListSelected
        val horizontalLayoutManagaer =
            LinearLayoutManager(this@JSActListSelectFilter, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = horizontalLayoutManagaer

        adapterListSelected.onChangeListener { selecteds ->
            itemGroupSelected.dataSet = selecteds
            Params.finishListener?.invoke(params!!)
        }

        llContainerBody.addView(view)
    }

    private fun generateViewLinear(
        itemGroup: JSGroupFilterModel,
        itemGroupSelected: JSGroupFilterModel,
        indexGroup: Int
    ) {
        val view =
            LayoutInflater.from(this@JSActListSelectFilter).inflate(R.layout.js_item_group_selected_linear, null, false)
        (view.findViewById(R.id.tvTitleItemGroupSelectedLinear) as TextView).text = itemGroup.title
        val tvTitle = view.findViewById(R.id.tvDescriptionItemGroupSelectedLinear) as TextView
        val ivItemExists = view.findViewById(R.id.ivItemExists) as ImageView
        tvTitle.setTextColor(params!!.colorBackground)

        initItemsExists(ivItemExists, tvTitle, itemGroupSelected)

        (view.findViewById(R.id.llContainerBodyItemGroupSelectedLinear) as LinearLayout)
            .setOnClickListener {
                val intent = Intent(this@JSActListSelectFilter, ActSelectItems::class.java)

                val paramsSelectItems = ParamsSelectItems()
                paramsSelectItems.title = itemGroup.title
                paramsSelectItems.dataSet = itemGroup.dataSet
                paramsSelectItems.dataSetSelected = itemGroupSelected.dataSet
                paramsSelectItems.multiple = itemGroup.multiple
                paramsSelectItems.colorPrimary = params!!.colorBackground
                paramsSelectItems.indexGroup = indexGroup

                intent.putExtra(ActSelectItems.PARAMS_SELECT_ITEMS, paramsSelectItems)
                startActivityForResult(intent, ActSelectItems.RESULT)
//                val dlg = DlgSelectItems<JSItemFilterModel>(this@JSActListSelectFilter)
//                dlg.show(
//                    title = itemGroup.title,
//                    dataSet = itemGroup.dataSet,
//                    dataSetSelected = itemGroupSelected.dataSet,
//                    multiple = itemGroup.multiple,
//                    colorPrimary = params!!.colorBackground)
//
//                dlg.onItemsSelected { items ->
//                    itemGroupSelected.dataSet = items
//                    initItemsExists(ivItemExists, tvTitle, itemGroupSelected)
//                }
            }

        llContainerBody.addView(view)
    }

    private fun initItemsExists(ivItemExists: ImageView, tvTitle: TextView, itemGroupSelected: JSGroupFilterModel) {
        Util.loadTextView(tvTitle, getExtractItensSelected(itemGroupSelected.dataSet))

        if (itemGroupSelected.dataSet.size > 0) {
            ivItemExists.setImageDrawable(resources.getDrawable(R.drawable.js_close_silver))
            ivItemExists.setOnClickListener {
                itemGroupSelected.dataSet = ArrayList()
                initItemsExists(ivItemExists, tvTitle, itemGroupSelected)
            }
            return
        }

        ivItemExists.setImageDrawable(resources.getDrawable(R.drawable.chevron_right))
    }

    private fun getExtractItensSelected(dataSet: ArrayList<JSItemFilterModel>): String? {
        return dataSet.joinToString { item-> item.description }
    }

    private fun initToolbar() {
        if (params?.idDrawableIcon != null) {
            toobar.setNavigationIcon(params?.idDrawableIcon!!)
        } else {
            toobar.setNavigationIcon(R.drawable.js_close_emperor)
        }

        toobar.title = params?.title

        toobar.setNavigationOnClickListener {
            finish()
        }
        lineToobar.setBackgroundColor(params!!.colorBackground)
//        toobar.setBackgroundColor(params!!.colorBackground)
//        toobar.setTitleTextColor(params!!.colorText)
    }

    private fun initBtnFinish() {
        btnFinish.setTextColor(params!!.colorBackground)
        lineBtnFinish.setBackgroundColor(params!!.colorBackground)

        btnFinish.setOnClickListener {
            Params.finishListener?.invoke(params!!)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == ActSelectItems.RESULT && resultCode == Activity.RESULT_OK -> {
                val paramsSelectItems =
                    data?.getSerializableExtra(ActSelectItems.PARAMS_SELECT_ITEMS) as ParamsSelectItems
                getExtrateView(paramsSelectItems)
                Params.finishListener?.invoke(params!!)
            }
        }
    }

    private fun getExtrateView(paramsSelectItems: ParamsSelectItems) {

        val view = llContainerBody.getChildAt(paramsSelectItems.indexGroup)

        val tvTitle = view.findViewById(R.id.tvDescriptionItemGroupSelectedLinear) as TextView
        val ivItemExists = view.findViewById(R.id.ivItemExists) as ImageView
        tvTitle.setTextColor(params!!.colorBackground)
        params?.listDataSetFilterSelected!![paramsSelectItems.indexGroup].dataSet = paramsSelectItems.dataSetSelected

        initItemsExists(ivItemExists, tvTitle, params?.listDataSetFilterSelected!![paramsSelectItems.indexGroup])
    }
}
