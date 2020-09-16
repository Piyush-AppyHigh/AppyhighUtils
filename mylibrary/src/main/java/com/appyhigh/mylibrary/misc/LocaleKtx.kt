
package com.appyhigh.mylibrary.misc

import java.util.*


fun Locale.getDefaultLanguageCode(): String {
    val defaultLanguageCode = Locale.getDefault().language
    return if (defaultLanguageCode.isNullOrEmpty())
        Locale.US.language
    else
        defaultLanguageCode
}