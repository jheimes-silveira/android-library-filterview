package br.com.jheimesilveira.js.filterview.util

import android.view.View
import android.widget.TextView

object Util {

    fun loadTextView(textView: TextView, string: String?) {
        if (!string.isNullOrEmpty()) {
            textView.visibility = View.VISIBLE
            textView.text = string
            return
        }
        textView.visibility = View.GONE
    }
}