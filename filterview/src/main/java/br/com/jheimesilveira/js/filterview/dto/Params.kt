package br.com.jheimesilveira.js.filterview.dto

import java.io.Serializable
import java.util.ArrayList

class Params: Serializable {
    companion object {
        var finishListener: ((params: Params) -> Unit)? = null
    }
    var colorBackground: Int = 0
    var colorText: Int = 0
    var title: String? = null
    var listDataSetFilterSelected = ArrayList<JSGroupFilterModel>()
    var listDataSetFilter = ArrayList<JSGroupFilterModel>()
    var idDrawableIcon: Int? = null
}