package com.appyhigh

import com.appyhigh.mylibrary.BaseApp
import com.appyhigh.mylibrary.misc.Constants
import com.appyhigh.utils.R

class App : BaseApp() {
    override fun onCreate() {
        super.onCreate()
        Constants.FCM_TARGET_ACTIVIY = "com.appyhigh.MainActivity"
        Constants.FCM_ICON = R.mipmap.ic_launcher
    }
}