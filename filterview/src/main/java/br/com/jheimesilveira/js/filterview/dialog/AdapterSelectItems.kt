package br.com.jheimesilveira.js.filterview.dialog

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import br.com.jheimesilveira.js.filterview.R
import kotlinx.android.synthetic.main.js_select_items.view.*
import kotlin.collections.ArrayList




@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class AdapterSelectItems<T>(
    var mContext: Context,
    var dataSet: ArrayList<T>,
    var dataSetSelected: ArrayList<T>,
    var multiple: Boolean,
    val maxLines: Int? = null,
    var colorPrimary: Int? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var observerBtnSelected: ((dataSetSelected: ArrayList<T>) -> Unit)? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        return ItemMyViewHolder(
            LayoutInflater.from(viewGroup.context).inflate(
                R.layout.js_select_items,
                viewGroup,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        initItemMyViewHolder(holder, position)
    }

    fun add(item: T) {
        dataSet.add(item)
        notifyDataSetChanged()
    }

    /**
     * Comportamento ao carregar uma resposta
     */
    private fun initItemMyViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataSet[position]
        holder.itemView.tvDescription.text = item.toString()
        holder.itemView.cbSelect.visibility = if (multiple) View.VISIBLE else View.GONE
        holder.itemView.cbSelect.isChecked = dataSetSelected.contains(item)

        holder.itemView.cbSelect.setOnClickListener {
            setAddItem(item, holder.itemView.cbSelect)
            notifyItemChanged(position)
        }

        holder.itemView.rlContainer.setOnClickListener {
            setAddItem(item, holder.itemView.cbSelect)
        }
    }

    private fun setAddItem(item: T, cbSelect: CheckBox) {
        if (!multiple) {
            dataSetSelected = ArrayList()
            dataSetSelected.add(item)
            notifyDataSetChanged()
            observerBtnSelected?.invoke(dataSetSelected)
            return
        }

        if (dataSetSelected.contains(item)) {
            dataSetSelected.remove(item)
        } else {
            dataSetSelected.add(item)
        }
        cbSelect.isChecked = !cbSelect.isChecked
        observerBtnSelected?.invoke(dataSetSelected)
    }

    fun onObserverBtnSelected(observerBtnSelected: (dataSetSelected: ArrayList<T>) -> Unit) {
        this.observerBtnSelected = observerBtnSelected
    }

    /**
     * Layout para carregar uma resposta
     */
    inner class ItemMyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}