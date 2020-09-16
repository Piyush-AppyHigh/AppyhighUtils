package com.appyhigh.mylibrary.view

import android.text.method.LinkMovementMethod
import android.widget.TextView
import com.appyhigh.mylibrary.misc.getHtmlFormattedSpanned

/**
 * Set HTML String with links enabled
 */
fun TextView.setHtmlText(htmlString: String) {
    // Enables all anchor links inside the text view
    this.movementMethod = LinkMovementMethod.getInstance()
    this.text = htmlString.getHtmlFormattedSpanned()
}