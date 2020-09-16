package com.appyhigh.mylibrary.view

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat

fun ImageView.setDrawableAsCircular(drawable: Drawable?) {
    drawable?.let {
        this.setImageDrawable(drawable)
        this.clipToCircle()
    }
}

fun ImageView.setDrawableAsCircular(@DrawableRes drawableRes: Int) {
    val drawable = ContextCompat.getDrawable(this.context, drawableRes)
    this.setDrawableAsCircular(drawable)
}

fun ImageView.setTintColor(@ColorRes colorRes: Int) = ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(ContextCompat.getColor(this.context, colorRes)))

fun ImageView.setDrawable(@DrawableRes drawableRes: Int) = setImageDrawable(ContextCompat.getDrawable(this.context, drawableRes))
