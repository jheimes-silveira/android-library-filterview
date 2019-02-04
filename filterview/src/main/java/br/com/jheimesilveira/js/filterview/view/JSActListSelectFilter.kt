package br.com.jheimesilveira.js.filterview.view

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
import br.com.jheimesilveira.js.filterview.dialog.DlgSelectItems
import br.com.jheimesilveira.js.filterview.dto.JSGroupFilterModel
import br.com.jheimesilveira.js.filterview.dto.JSItemFilterModel
import br.com.jheimesilveira.js.filterview.dto.Params
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
                        params!!.listDataSetFilterSelected[itemGroup.index])
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
        }

        llContainerBody.addView(view)
    }

    private fun generateViewLinear(
        itemGroup: JSGroupFilterModel,
        itemGroupSelected: JSGroupFilterModel
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
                val dlg = DlgSelectItems<JSItemFilterModel>(this@JSActListSelectFilter)
                dlg.show(
                    title = itemGroup.title,
                    dataSet = itemGroup.dataSet,
                    dataSetSelected = itemGroupSelected.dataSet,
                    multiple = itemGroup.multiple,
                    colorPrimary = params!!.colorBackground)

                dlg.onItemsSelected { items ->
                    itemGroupSelected.dataSet = items
                    initItemsExists(ivItemExists, tvTitle, itemGroupSelected)
                }
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
            toobar.setNavigationIcon(R.drawable.js_close)
        }

        toobar.title = params?.title

        toobar.setNavigationOnClickListener {
            finish()
        }
        toobar.setBackgroundColor(params!!.colorBackground)
        toobar.setTitleTextColor(params!!.colorText)
    }

    private fun initBtnFinish() {
        btnFinish.setBackgroundColor(params!!.colorBackground)
        btnFinish.setOnClickListener {
            Params.finishListener?.invoke(params!!)
            finish()
        }
    }
}
