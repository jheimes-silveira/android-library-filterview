package br.com.jheimesilveira.js.filterview.dto

import java.io.Serializable

class JSGroupFilterModel(
    var title: String,
    var dataSet: ArrayList<JSItemFilterModel> = ArrayList(),
    var type: Type = Type.LINEAR,
    var multiple: Boolean = false
): Serializable {

    enum class Type constructor(val id: Int) {
        LINEAR(1),
        GRID(2),
        PROGRESS(3);
        companion object {
            fun getById(id: Int): Type? {
                for (c in Type.values()) {
                    if (c.id == id) {
                        return c
                    }
                }
                return null
            }
        }
    }
}