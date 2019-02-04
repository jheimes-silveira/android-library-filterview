package br.com.jheimesilveira.js.filterview.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.jheimesilveira.js.filterview.R
import br.com.jheimesilveira.js.filterview.dto.JSItemFilterModel
import kotlinx.android.synthetic.main.js_item_selected.view.*

class AdapterListSelected(var mContext: Context, var dataSet: ArrayList<JSItemFilterModel> = ArrayList(),
                          val maxLines: Int? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER = 0
    private val TYPE_FOOTER = 1
    private val TYPE_ITEM = 2

    private var changeListener: ((jsItemFilterSelected: JSItemFilterModel) -> Unit)? = null

    /**
     * Define qual layout irá exibir no adapter
     */
    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_HEADER
            dataSet.size + 1 -> TYPE_FOOTER
            else -> TYPE_ITEM
        }
    }

    fun add(item: JSItemFilterModel) {
        val size = dataSet.size
        dataSet.add(item)
        notifyItemInserted(size + 1)
    }

    fun update(item: JSItemFilterModel, position: Int = dataSet.size - 1) {
        dataSet[position] = item
        notifyItemChanged(position + 1)
    }

    fun update(dataSet: ArrayList<JSItemFilterModel> = this.dataSet) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    fun remove(item: JSItemFilterModel) {
        dataSet.remove(item)
        update(dataSet)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        return when (i) {
            TYPE_HEADER -> {
                HeaderMyViewHolder(LayoutInflater.from(viewGroup.context)
                        .inflate(R.layout.js_item_selected_margin, viewGroup, false))
            }
            TYPE_FOOTER -> {
                FooterMyViewHolder(LayoutInflater.from(viewGroup.context)
                        .inflate(R.layout.js_item_selected_margin, viewGroup, false))
            }
            else -> {
                ItemMyViewHolder(LayoutInflater.from(viewGroup.context)
                        .inflate(R.layout.js_item_selected, viewGroup, false))
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size + 2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderMyViewHolder) {
            initHeaderMyViewHoder(holder)
            return
        }
        if (holder is FooterMyViewHolder) {
            initFooterMyViewHoder(holder)
            return
        }
        if (holder is ItemMyViewHolder) {
            initItemMyViewHolde(holder, position)
            return
        }
    }

    fun onChangeListener(changeListener: (jsItemFilterSelected: JSItemFilterModel) -> Unit) {
        this.changeListener = changeListener
    }

    /**
     * Comportamento ao carregar o cabeçalho
     */
    private fun initHeaderMyViewHoder(holder: HeaderMyViewHolder) {

    }

    /**
     * Carregar o rodapé
     */
    private fun initFooterMyViewHoder(holder: FooterMyViewHolder) {

    }

    /**
     * Comportamento ao carregar uma resposta
     */
    private fun initItemMyViewHolde(holder: ItemMyViewHolder, position: Int) {
        val item = dataSet[position - 1]
        holder.itemView.tvDescription.text = item.description
        holder.itemView.ivRemovedItem.setOnClickListener {
            remove(item)
            changeListener?.invoke(item)
        }
    }


    /**
     * Layout para carregar uma pergunta
     */
    inner class ItemMyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /**
     * Layout cabeçalho
     */
    inner class HeaderMyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /**
     * Layout rodapé
     */
    inner class FooterMyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}