package br.com.jheimesilveira.js.filterview

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import br.com.jheimesilveira.js.filterview.adapter.AdapterListSelected
import br.com.jheimesilveira.js.filterview.dto.JSGroupFilterModel
import br.com.jheimesilveira.js.filterview.dto.JSItemFilterModel
import br.com.jheimesilveira.js.filterview.dto.Params
import br.com.jheimesilveira.js.filterview.view.JSActListSelectFilter

class FilterView @JvmOverloads constructor(var mContext: Context, attrs: AttributeSet? = null) :
    LinearLayout(mContext, attrs) {

    lateinit var tvHint: TextView
    lateinit var rvListSelected: RecyclerView
    lateinit var ivFilter: ImageView
    lateinit var adapterListSelected: AdapterListSelected

    private var clickFilter: (() -> Unit)? = null
    private var changeGroupSelected: ((index: Int, jsGroupFilterModel: JSGroupFilterModel) -> Unit)? = null

    var showViewitemsSelectedInFilter = true
    set(value) {
        field = value
        setDataSetAdapterChip()
    }

    companion object {
        var finishActivity: (() -> Unit)? = null
    }

    var dialogBackgroundColor: Int = 0
    var dialogTextColor: Int = 0

    private var listDataSetFilterSelected = ArrayList<JSGroupFilterModel>()

    private var listDataSetFilter = ArrayList<JSGroupFilterModel>()

    private var dialogParams = Params()

    fun onShowSwipper(show: Boolean) {
        JSActListSelectFilter.onShowSwipper(show)
    }

    private var finishListener: (
        (
        listDataSetFilter: ArrayList<JSGroupFilterModel>,
        listDataSetFilterSelected: ArrayList<JSGroupFilterModel>,
        dataSet: ArrayList<JSItemFilterModel>
    ) -> Unit)? = null

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.JSFilterView, 0, 0)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.js_filterview, this, true)
        initViews()

        tvHint.text = typedArray.getString(R.styleable.JSFilterView_js_hint)
        dialogParams.title = typedArray.getString(R.styleable.JSFilterView_js_dialog_text)
        tvHint.setTextColor(
            typedArray.getColor(
                R.styleable.JSFilterView_js_hint_color,
                resources.getColor(R.color.primary_dark_material_light)
            )
        )

        dialogBackgroundColor = typedArray.getColor(
            R.styleable.JSFilterView_js_dialog_background_color,
            resources.getColor(R.color.picton_blue)
        )
        dialogTextColor = typedArray.getColor(
            R.styleable.JSFilterView_js_dialog_text_color,
            resources.getColor(android.R.color.white)
        )


        val iconFilter = typedArray.getDrawable(R.styleable.JSFilterView_js_icon_filter)
        if (iconFilter == null) {
            ivFilter.setImageDrawable(resources.getDrawable(R.drawable.js_filter_variant))
        } else {
            ivFilter.setImageDrawable(iconFilter)
        }

        dialogParams.idDrawableIcon = typedArray.getResourceId(
            R.styleable.JSFilterView_js_dialog_icon_filter, R.drawable.js_close_emperor
        )

        if (iconFilter == null) {
            ivFilter.setImageDrawable(resources.getDrawable(R.drawable.js_filter_variant))
        } else {
            ivFilter.setImageDrawable(iconFilter)
        }

        typedArray.recycle()
        initIvFilter()
        initAdapter()
        initResultActivity()
    }

    private fun initResultActivity() {
        finishActivity = {

            listDataSetFilterSelected.map { groupSelected ->
                groupSelected.dataSet = ArrayList()
            }

            adapterListSelected.dataSet.map { item ->
                listDataSetFilterSelected[item.indexGroup!!].dataSet.add(item)
            }

            setDataSetAdapterChip()
        }
    }

    /**
     * Loads all Filter Models
     * @param dataSet Model
     */
    fun loadDataSetFilter(models: ArrayList<JSGroupFilterModel>) {
        listDataSetFilter = ArrayList()
        listDataSetFilterSelected = ArrayList()

        for (i in models.withIndex()) {
            val jsItemFilterSelecteds = ArrayList<JSItemFilterModel>()
            for (j in i.value.dataSet.withIndex()) {
                jsItemFilterSelecteds.add(
                    JSItemFilterModel(
                        description = j.value.description,
                        value = j.value.value,
                        indexGroup = i.index,
                        groupId = i.value.id
                    )
                )
            }

            listDataSetFilter.add(
                JSGroupFilterModel(
                    i.value.title,
                    jsItemFilterSelecteds,
                    i.value.type,
                    i.value.multiple,
                    id = i.value.id
                )
            )

            listDataSetFilterSelected.add(
                JSGroupFilterModel(
                    i.value.title,
                    ArrayList(),
                    i.value.type,
                    i.value.multiple,
                    i.value.id
                )
            )
        }
    }

    fun loadDataSetFilterSelected(
        indexGroup: Int,
        dataSetSelected: (
            groupFilter: JSGroupFilterModel,
            groupFilterSelected: JSGroupFilterModel
        ) -> JSGroupFilterModel
    ) {
        if (indexGroup < 0 || indexGroup >= listDataSetFilterSelected.size) {
            throw IllegalArgumentException("Index fora do intervalo dos itens")
        }

        listDataSetFilterSelected[indexGroup] = dataSetSelected(
            listDataSetFilter[indexGroup],
            listDataSetFilterSelected[indexGroup]
        )

        dialogParams.listDataSetFilterSelected = listDataSetFilterSelected
        dialogParams.listDataSetFilter = listDataSetFilter
        dialogParams.colorBackground = dialogBackgroundColor
        dialogParams.colorText = dialogTextColor

        Params.changeDialogListener?.invoke(dialogParams)
    }

    /**
     * Any changes made to the selected objects this method will be called
     */
    fun onFinishListener(
        finishListener: (
            (
            listDataSetFilter: ArrayList<JSGroupFilterModel>,
            listDataSetFilterSelected: ArrayList<JSGroupFilterModel>,
            dataSet: ArrayList<JSItemFilterModel>
        ) -> Unit)
    ) {

        this.finishListener = finishListener
    }

    fun onChangeGroupSelected(changeGroupSelected: (index: Int, jsGroupFilterModel: JSGroupFilterModel) -> Unit) {
        this.changeGroupSelected = changeGroupSelected
    }

    fun onClickFilter(clickFilter: () -> Unit) {
        this.clickFilter = clickFilter
    }

    private fun initIvFilter() {
        Params.finishListener = { params ->
            listDataSetFilter = params.listDataSetFilter
            listDataSetFilterSelected = params.listDataSetFilterSelected
            adapterListSelected.dataSet = getExtractFilter(listDataSetFilterSelected)
            setDataSetAdapterChip()

            finishListener?.invoke(listDataSetFilter, listDataSetFilterSelected, adapterListSelected.dataSet)
        }

        Params.changeGroupSelected = { index, jsGroupFilterModel ->
            changeGroupSelected?.invoke(index, jsGroupFilterModel)
        }

        ivFilter.setOnClickListener {
            val i = Intent(mContext, JSActListSelectFilter::class.java)
            dialogParams.colorBackground = this.dialogBackgroundColor
            dialogParams.colorText = this.dialogTextColor
            dialogParams.listDataSetFilter = this.listDataSetFilter
            dialogParams.listDataSetFilterSelected = this.listDataSetFilterSelected

            i.putExtra(JSActListSelectFilter.PARAMS, dialogParams)
            mContext.startActivity(i)
            clickFilter?.invoke()
        }
    }

    private fun getExtractFilter(listDataSetFilterSelected: ArrayList<JSGroupFilterModel>): ArrayList<JSItemFilterModel> {
        val array = ArrayList<JSItemFilterModel>()
        listDataSetFilterSelected.map { filterGroup ->
            array.addAll(filterGroup.dataSet)
        }
        return array
    }

    /**
     * Componenete de dar dicas possíveis sobre perguntas que o usuário pode realizar
     */
    private fun initAdapter() {
        adapterListSelected = AdapterListSelected(mContext)
        rvListSelected.adapter = adapterListSelected
        val horizontalLayoutManagaer = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        rvListSelected.layoutManager = horizontalLayoutManagaer

        setDataSetAdapterChip(ArrayList())

        adapterListSelected.onChangeListener { item ->
            listDataSetFilterSelected[item.indexGroup!!].dataSet.remove(item)
            setDataSetAdapterChip()
            changeGroupSelected?.invoke(item.indexGroup, listDataSetFilterSelected[item.indexGroup])
        }
    }

    private fun setDataSetAdapterChip(arrayList: ArrayList<JSItemFilterModel> = adapterListSelected.dataSet) {
        if (showViewitemsSelectedInFilter && arrayList.size != 0) {
            rvListSelected.visibility = View.VISIBLE
        } else {
            rvListSelected.visibility = View.GONE
        }

        adapterListSelected.update(arrayList)
    }

    private fun initViews() {
        tvHint = findViewById(R.id.tvHint)
        rvListSelected = findViewById(R.id.rvListSelected)
        ivFilter = findViewById(R.id.ivFilter)
    }
}