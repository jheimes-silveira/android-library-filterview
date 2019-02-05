package br.com.jheimesilveira.js.filterview.dto

import java.io.Serializable

class ParamsSelectItems: Serializable {
    var title: String? = null
    var dataSet: ArrayList<JSItemFilterModel> = ArrayList()
    var dataSetSelected: ArrayList<JSItemFilterModel> = ArrayList()
    var multiple: Boolean = false
    var colorPrimary: Int = 0
    var indexGroup: Int = -1
}
