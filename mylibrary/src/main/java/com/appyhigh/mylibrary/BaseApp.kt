package com.appyhigh.mylibrary

import android.app.Application
import android.content.ComponentCallbacks
import android.content.res.Configuration
import com.appyhigh.mylibrary.misc.Constants.FCM_DEBUG_TOPIC
import com.appyhigh.mylibrary.misc.Constants.FCM_RELEASE_TOPIC
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging

open class BaseApp : Application() {
    override fun registerComponentCallbacks(callback: ComponentCallbacks?) {
        super.registerComponentCallbacks(callback)
    }

    override fun unregisterComponentCallbacks(callback: ComponentCallbacks?) {
        super.unregisterComponentCallbacks(callback)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        FirebaseAnalytics.getInstance(this)
        if (BuildConfig.DEBUG)
            FirebaseMessaging.getInstance().subscribeToTopic(FCM_DEBUG_TOPIC)
        else
            FirebaseMessaging.getInstance().subscribeToTopic(FCM_RELEASE_TOPIC)

        MobileAds.initialize(this)
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    override fun registerActivityLifecycleCallbacks(callback: ActivityLifecycleCallbacks?) {
        super.registerActivityLifecycleCallbacks(callback)
    }

    override fun unregisterActivityLifecycleCallbacks(callback: ActivityLifecycleCallbacks?) {
        super.unregisterActivityLifecycleCallbacks(callback)
    }

    override fun registerOnProvideAssistDataListener(callback: OnProvideAssistDataListener?) {
        super.registerOnProvideAssistDataListener(callback)
    }

    override fun unregisterOnProvideAssistDataListener(callback: OnProvideAssistDataListener?) {
        super.unregisterOnProvideAssistDataListener(callback)
    }
}