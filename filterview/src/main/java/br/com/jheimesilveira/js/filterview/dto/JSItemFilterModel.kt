package br.com.jheimesilveira.js.filterview.dto

import java.io.Serializable

class JSItemFilterModel(
    var description: String,
    var value: Any? = null,
    val group: Int? = null): Serializable {

    override fun toString(): String {
        return description
    }
}
