package com.appyhigh

import android.os.Bundle
import com.appyhigh.mylibrary.activity.toast
import com.appyhigh.mylibrary.ads.AdCloseListener
import com.appyhigh.mylibrary.ads.loadInterstitial
import com.appyhigh.mylibrary.ads.loadTemplateNativeAd
import com.appyhigh.mylibrary.ads.newInterstitialAd
import com.appyhigh.utils.R
import com.google.android.gms.ads.InterstitialAd
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : BaseActivity(), AdCloseListener {
    lateinit var ad: InterstitialAd
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        ad = newInterstitialAd(
            resources.getString(R.string.test_interstitial),
            this@MainActivity, "app_exit", this
        )
        loadInterstitial(ad)
        loadTemplateNativeAd(
            context = this,
            id = getString(R.string.test_native),
            templateView = template_ad_small,
            screen = "main"
        )
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.activity_main
    }

    override fun onBackPressed() {
        if (ad.isLoaded) {
            ad.show()
        } else {
            exitAlert()
        }
    }

    override fun adClosed() {
        exitAlert()
    }

    private fun exitAlert() {
        toast("hahahhahaha", 1)
        finish()
    }
}