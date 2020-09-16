package com.appyhigh.mylibrary.misc

import android.text.Html
import android.text.Spanned
import java.security.MessageDigest
import java.util.*


fun String.removeLastChar(endingChar: String): String {
    var stringText = this
    if (!stringText.isEmpty()) {
        if (stringText.endsWith(endingChar)) {
            stringText = stringText.substring(0, stringText.length - 1)
        }
    }
    return stringText
}

fun String.toTitleCase(): String {
    val titleCase = StringBuilder()
    var nextTitleCase = true

    for (c in this.toCharArray()) {
        var character = c
        if (Character.isSpaceChar(c)) {
            nextTitleCase = true
        } else if (nextTitleCase) {
            character = Character.toTitleCase(c)
            nextTitleCase = false
        }
        titleCase.append(character)
    }

    return titleCase.toString()
}

fun String.md5() = encrypt(this, "MD5")

fun String.sha1() = encrypt(this, "SHA-1")

fun String.encrypt(string: String?, type: String): String {
    val bytes = MessageDigest.getInstance(type).digest(string?.toByteArray()!!)
    return bytes.toHex()
}

fun String.equalsIgnoreCase(other: String) =
    this.toLowerCase(Locale.ROOT).contentEquals(other.toLowerCase(Locale.ROOT))

fun String.isPhone(): Boolean {
    val p = "^1([34578])\\d{9}\$".toRegex()
    return matches(p)
}

fun String.isEmail(): Boolean {
    val p = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)\$".toRegex()
    return matches(p)
}

fun String.isNumeric(): Boolean {
    val p = "^[0-9]+$".toRegex()
    return matches(p)
}

fun String.getHtmlFormattedSpanned(): Spanned {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this)
    }
}