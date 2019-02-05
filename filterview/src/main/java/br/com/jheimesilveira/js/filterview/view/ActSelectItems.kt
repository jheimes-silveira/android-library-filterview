package br.com.jheimesilveira.js.filterview.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import br.com.jheimesilveira.js.filterview.R
import br.com.jheimesilveira.js.filterview.dialog.AdapterSelectItems
import br.com.jheimesilveira.js.filterview.dto.JSItemFilterModel
import br.com.jheimesilveira.js.filterview.dto.ParamsSelectItems
import kotlinx.android.synthetic.main.dlg_select_items.*


/**
 * Created by jheimesilveira.
 * @author jheimes.silveira@gmail.com
 */
class ActSelectItems<T> : AppCompatActivity() {

    companion object {
        const val PARAMS_SELECT_ITEMS = "PARAMS_SELECT_ITEMS"
        const val INDEX_GROUP = "INDEX_GROUP"
        const val RESULT = 3412
    }

    var adapter: AdapterSelectItems<T>? = null

    var paramsSelectItems: ParamsSelectItems? = null

    var colorPrimary: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_select_items)
        initComponents()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun initComponents() {
        paramsSelectItems = intent.getSerializableExtra(PARAMS_SELECT_ITEMS) as ParamsSelectItems?
        onBtnCancel()
        this.colorPrimary = paramsSelectItems!!.colorPrimary
        initList(
            paramsSelectItems!!.dataSet as ArrayList<T>,
            paramsSelectItems!!.dataSetSelected as ArrayList<T>,
            paramsSelectItems!!.multiple
        )
        title = paramsSelectItems!!.title
        initBtnSelect()
        initToobar()
    }

    private fun initToobar() {
        lineToolbar.setBackgroundColor(colorPrimary!!)

        if (!adapter!!.multiple) {
            return
        }

        toolbar.inflateMenu(R.menu.js_menu_dlg_selected_multiple)
        setTextMenuItemSelected()

        toolbar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.nav_selected_all) {
                setMenuItemSelected()
                setTextMenuItemSelected()
            }

            true

        }
    }

    private fun setMenuItemSelected() {
        if (adapter?.dataSetSelected?.size!! > 0) {
            adapter?.dataSetSelected = ArrayList()
            enableButtomApply(false)
            adapter?.notifyDataSetChanged()
        } else {
            adapter?.dataSetSelected = adapter?.dataSet!!.clone() as ArrayList<T>
            enableButtomApply(true)
            adapter?.notifyDataSetChanged()
        }
    }

    private fun setTextMenuItemSelected() {
        val txtMenu = if (adapter?.dataSetSelected?.size!! > 0) {
            "Limpar todos"
        } else {
            "Selecionar todos"
        }
        val s = SpannableString(txtMenu)
        s.setSpan(ForegroundColorSpan(colorPrimary!!), 0, s.length, 0)
        toolbar.menu.getItem(0).title = s
    }

    fun getBtnSelect(): Button {
        return btnApply
    }

    fun setTitle(txt: String?) {
        toolbar.title = txt
    }

    private fun initBtnSelect() {
        getBtnSelect().visibility = if (adapter!!.multiple) View.VISIBLE else View.GONE

        btnApply.setOnClickListener {
            paramsSelectItems?.dataSetSelected = adapter?.dataSetSelected as ArrayList<JSItemFilterModel>
            val returnIntent = Intent()
            returnIntent.putExtra(PARAMS_SELECT_ITEMS, paramsSelectItems)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        lineBtnApply.setBackgroundColor(colorPrimary!!)
        btnApply.setTextColor(colorPrimary!!)
    }

    private fun initList(
        dataSet: ArrayList<T>,
        dataSetSelected: ArrayList<T>,
        multiple: Boolean
    ) {
        adapter = AdapterSelectItems(this@ActSelectItems, dataSet, dataSetSelected, multiple)
        val linearLayoutManager = LinearLayoutManager(this@ActSelectItems)
        rvList.layoutManager = linearLayoutManager
        rvList.adapter = adapter
        adapter?.onObserverBtnSelected { items ->
            if (!adapter!!.multiple) {
                paramsSelectItems?.dataSetSelected = adapter?.dataSetSelected as ArrayList<JSItemFilterModel>
                val returnIntent = Intent()
                returnIntent.putExtra(PARAMS_SELECT_ITEMS, paramsSelectItems)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
                return@onObserverBtnSelected
            }

            enableButtomApply(items.size > 0)
            setTextMenuItemSelected()
        }

        enableButtomApply(dataSetSelected.size > 0)
    }

    private fun enableButtomApply(status: Boolean) {
        if (status) {
            lineBtnApply.alpha = 1f
            btnApply.alpha = 1f
            btnApply.isEnabled = true
            return
        }
        btnApply.alpha = 0.3f
        lineBtnApply.alpha = 0.3f
        btnApply.isEnabled = false
    }

    private fun onBtnCancel() {
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}
