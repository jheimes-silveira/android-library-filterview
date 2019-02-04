package br.com.jheimesilveira.js.filterview.dialog

import android.app.Dialog
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import br.com.jheimesilveira.js.filterview.R
import kotlinx.android.synthetic.main.dlg_select_items.*

/**
 * Created by jheimesilveira.
 * @author jheimes.silveira@gmail.com
 */
class DlgSelectItems<T>

/**
 * Construtor
 * @param context
 */
(context: Context) : Dialog(context) {

    var adapter: AdapterSelectItems<T>? = null

    var itemsSelected: ((dataSetSelected: ArrayList<T>) -> Unit)? = null

    var colorPrimary: Int? = null

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dlg_select_items)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        onBtnCancel()
    }

    private fun initToobar() {
        if (!adapter!!.multiple) {
            return
        }
        toolbar.inflateMenu(R.menu.js_menu_dlg_selected_multiple)
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_selected_all -> {
                    adapter?.dataSetSelected = adapter?.dataSet!!.clone() as ArrayList<T>
                    enableButtomApply(true)
                    adapter?.notifyDataSetChanged()
                    true
                }
                else -> {
                    adapter?.dataSetSelected = ArrayList()
                    enableButtomApply(false)
                    adapter?.notifyDataSetChanged()
                    true
                }
            }
        }
    }

    fun getTitle(): String? {
        return toolbar.title.toString()
    }

    fun getBtnSelect(): Button {
        return btnApply
    }

    fun setTitle(txt: String?) {
        toolbar.title = txt
    }

    fun show(title: String? = null,
             dataSet: ArrayList<T> = ArrayList(),
             dataSetSelected: ArrayList<T> = ArrayList(),
             multiple: Boolean = false,
             colorPrimary: Int = context.resources.getColor(R.color.picton_blue)) {
        this.colorPrimary = colorPrimary
        initList(dataSet, dataSetSelected, multiple)
        setTitle(title)
        initBtnSelect()
        initToobar()
        super.show()
    }

    fun onItemsSelected(selected: ((items: ArrayList<T>) -> Unit)? = null) {
        this.itemsSelected = selected
    }

    private fun initBtnSelect() {
        getBtnSelect().visibility = if (adapter!!.multiple) View.VISIBLE else View.GONE

        btnApply.setOnClickListener {
            itemsSelected?.invoke(adapter!!.dataSetSelected)
            dismiss()
        }

        btnApply.setBackgroundColor(colorPrimary!!)
    }

    private fun initList(
        dataSet: ArrayList<T>,
        dataSetSelected: ArrayList<T>,
        multiple: Boolean
    ) {
        adapter = AdapterSelectItems(context,  dataSet, dataSetSelected, multiple)
        val linearLayoutManager = LinearLayoutManager(context)
        rvList.layoutManager = linearLayoutManager
        rvList.adapter = adapter
        adapter?.onObserverBtnSelected { items ->
            enableButtomApply(items.size > 0)

            if (!adapter!!.multiple) {
                itemsSelected?.invoke(adapter!!.dataSetSelected)
                dismiss()
            }
        }

        enableButtomApply(dataSetSelected.size > 0)
    }

    private fun enableButtomApply(status: Boolean) {
        if (status) {
            btnApply.alpha = 1f
            btnApply.isEnabled = true
            return
        }
        btnApply.alpha = 0.5f
        btnApply.isEnabled = false
    }

    private fun onBtnCancel() {
        toolbar.setNavigationOnClickListener {
            dismiss()
        }
    }
}
