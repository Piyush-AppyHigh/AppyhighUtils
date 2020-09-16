package com.appyhigh.mylibrary.ads

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.appyhigh.mylibrary.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import kotlinx.android.synthetic.main.native_ad_big_dashboard.view.*

fun newInterstitialAd(
    ad_unit: String,
    activity: AppCompatActivity,
    screen: String, closeListener: AdCloseListener
): InterstitialAd {
    val interstitialAd = InterstitialAd(activity)
    interstitialAd.adUnitId = ad_unit
    interstitialAd.adListener = object : AdListener() {
        override fun onAdImpression() {
            super.onAdImpression()
        }

        override fun onAdLeftApplication() {
            super.onAdLeftApplication()
        }

        override fun onAdClicked() {
            super.onAdClicked()
        }

        override fun onAdFailedToLoad(p0: Int) {
            super.onAdFailedToLoad(p0)
        }

        override fun onAdClosed() {
            super.onAdClosed()
            closeListener.adClosed()
        }

        override fun onAdOpened() {
            super.onAdOpened()
        }

        override fun onAdLoaded() {
            super.onAdLoaded()
        }
    }
    return interstitialAd
}

fun tryToLoadAdOnceAgain(
    ad_unit: String,
    activity: AppCompatActivity, listener: AdCloseListener
) {
    loadInterstitial(
        newInterstitialAd(
            ad_unit,
            activity, "app_exit", listener
        )
    )
}


fun loadInterstitial(mInterstitialAd: InterstitialAd) {
    val adRequest = AdRequest.Builder().build()
    mInterstitialAd.loadAd(adRequest)
}


fun loadOtherAd(context: Context, id: String, nativeAdArea: LinearLayout, screen: String) {
    val adLoader =
        AdLoader.Builder(context, id)
            .forUnifiedNativeAd { unifiedNativeAd: UnifiedNativeAd ->
                val adView = LayoutInflater.from(context)
                    .inflate(R.layout.native_ad_big_dashboard, null) as UnifiedNativeAdView
                populateUnifiedNativeAdView(unifiedNativeAd, adView)
                nativeAdArea.removeAllViews()
                nativeAdArea.addView(adView)
            }.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(errorCode: Int) {
                    // Handle the failure by logging, altering the UI, and so on.

                }

            }).withNativeAdOptions(NativeAdOptions.Builder().build()).build()
    adLoader.loadAd(AdRequest.Builder().build())
}

private fun populateUnifiedNativeAdView(
    unifiedNativeAd: UnifiedNativeAd,
    adView: UnifiedNativeAdView
) {

    val adHeadline = adView.ad_headlines as TextView
    adView.headlineView = adHeadline
    (adView.headlineView as TextView).text = unifiedNativeAd.headline

    val adBody = adView.ad_body_text as TextView
    adView.bodyView = adBody
    (adView.bodyView as TextView).text = unifiedNativeAd.body

    val cta = adView.ad_call_to_action_button as Button
    adView.callToActionView = cta
    (adView.callToActionView as Button).text = unifiedNativeAd.callToAction

    val iconView = adView.ad_icons as ImageView
    Log.e("nativead", "ad body : " + unifiedNativeAd.body)

    val icon = unifiedNativeAd.icon
    adView.iconView = iconView
    if (icon == null) {
        adView.iconView.visibility = View.GONE
    } else {
        (adView.iconView as ImageView).setImageDrawable(icon.drawable)
        adView.iconView.visibility = View.VISIBLE
    }

    val price = adView.ad_price_text as TextView
    adView.priceView = price
    if (unifiedNativeAd.price == null) {
        adView.priceView.visibility = View.GONE
    } else {
        adView.priceView.visibility = View.VISIBLE
        (adView.priceView as TextView).text = unifiedNativeAd.price
    }

    val store = adView.ad_store_text as TextView
    adView.storeView = store
    if (unifiedNativeAd.store == null) {
        adView.storeView.visibility = View.INVISIBLE
    } else {
        adView.storeView.visibility = View.VISIBLE
        (adView.storeView as TextView).text = unifiedNativeAd.store
    }

    val ratingBar = adView.ad_stars_bar as View
    adView.starRatingView = ratingBar
    if (unifiedNativeAd.starRating == null) {
        adView.starRatingView.visibility = View.INVISIBLE
    } else {
        (adView.starRatingView as RatingBar).rating = unifiedNativeAd.starRating!!.toFloat()
        adView.starRatingView.visibility = View.VISIBLE
    }

    val advertiser = adView.ad_advertiser_text as TextView
    adView.advertiserView = advertiser
    if (unifiedNativeAd.advertiser == null) {
        adView.advertiserView.visibility = View.INVISIBLE
    } else {
        (adView.advertiserView as TextView).text = unifiedNativeAd.advertiser
        adView.advertiserView.visibility = View.VISIBLE
    }

    val mediaView = adView.ad_media as MediaView
    adView.mediaView = mediaView

    adView.setNativeAd(unifiedNativeAd)
}


fun loadTemplateNativeAd(
    context: Context,
    id: String,
    templateView: TemplateView,
    screen: String
) {
    val builder = AdLoader.Builder(
        context, id
    )

    builder.forUnifiedNativeAd { unifiedNativeAd ->
        templateView.setNativeAd(unifiedNativeAd)
        templateView.visibility = View.GONE
    }

    val adLoader = builder
        .withAdListener(object : AdListener() {
            override fun onAdImpression() {
                super.onAdImpression()
            }

            override fun onAdLeftApplication() {
                super.onAdLeftApplication()
            }

            override fun onAdClicked() {
                super.onAdClicked()
            }

            override fun onAdFailedToLoad(p0: Int) {
                templateView.visibility = View.GONE
                super.onAdFailedToLoad(p0)
            }

            override fun onAdClosed() {
                super.onAdClosed()
            }

            override fun onAdOpened() {
                super.onAdOpened()
            }

            override fun onAdLoaded() {
                templateView.visibility = View.VISIBLE
                super.onAdLoaded()
            }
        }).build()
    adLoader.loadAd(AdRequest.Builder().build())
}

interface AdCloseListener {
    fun adClosed()
}



