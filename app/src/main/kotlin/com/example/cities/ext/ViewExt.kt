package com.example.cities.ext

import android.os.Build
import android.text.Html
import android.view.View
import android.widget.TextView

inline fun View.gone() {
    if (visibility != View.GONE) visibility = View.GONE
}

inline fun View.show() {
    if (visibility != View.VISIBLE) visibility = View.VISIBLE
}

inline fun TextView.setHtmlText(html: String) {
    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
    else Html.fromHtml(html)
}