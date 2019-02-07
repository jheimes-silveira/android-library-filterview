package br.com.jheimesilveira.js.filterview.dto

import java.io.Serializable

class JSItemFilterModel(
    var description: String,
    var value: Any? = null,
    var showInToobarSelected: Boolean = true,
    val indexGroup: Int? = null,
    val groupId: String? = null): Serializable {

    override fun equals(other: Any?): Boolean {
        return other == description
    }

    override fun toString(): String {
        return description
    }
}
