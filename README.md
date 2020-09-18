# AppyhighUtils
lib for android devs



1. Import the library by adding the following line to your app level build.gradle file.
```
implementation 'com.github.PrasadVennamAppy:AppyhighUtils:[![](https://jitpack.io/v/PrasadVennamAppy/AppyhighUtils.svg)](https://jitpack.io/#PrasadVennamAppy/AppyhighUtils)
```
This library consists of following modules

    1) CleverTap and Firebase Events
    2) Push Notifications (CleverTap and Firebase)
    3) InApp Notifications (CleverTap and Firebase)
    4) Ads (Interstitial and Native)
    5) Dynamic Linking
    6) Apxor Events
  
## CleverTap and Firebase Events

A boolean value 'onlyFirebase' needs to be provided as parameter while using this module

'onlyFirebase'  - *true*  - Only Firebase Cloud Messaging is used for events

'onlyFirebase'  - *false* - Both CleverTap and Firebase Cloud Messaging can be used for events

## Push Notifications (CleverTap and Firebase)

CleverTap and Firebase can be used for push notifications in this library.

* We have 4 types of push notifications **P**, **B**, **L** and **D** which are identified by the *which* parameter.
* ***to*** can have two values *fcmToken* or *topicName*
* **P** type notifications must open the play store. ("url": "com.appyhigh")
* **B** type notifications must open the default browser. ("url": "https://youtube.com")
* **L** type notifications must open the webview within the app. ("url": "https://youtube.com")
* **D** type notification must open a specific page within the app. ("url": "AppName://ACTIVITYNAME")

Example data format to send for push notifications
```
{
  "to": "/topics/Appname",
  "data": {
    "title": "Krissh 3",
    "message": "See hritiks best action super hero movie only on liveTv",
    "image": "https://img.youtube.com/vi/1N_zzi2ad04/hqdefault.jpg",
    "url": "https://www.youtube.com/watch?v=ZpuY57qrZs8",
    "which": "L"
  }
}
```

1. Call *checkForNotifications* from your *MainActivity*
```
checkForNotifications(
            context: Context,
            intent: Intent,
            webViewActivity: Class<out Activity?>?
        )
```

#### Example
```
MyFirebaseMessaging.checkForNotifications(context = this, intent = intent, webViewActivity = WebViewActivity::class.java)
```


## InApp Notifications (CleverTap and Firebase)

## Ads (Interstitial and Native)

1. Add the following lines to your app-level build.gradle file.
```
implementation 'com.google.android.gms:play-services-ads:{latestVersion}
```
2. Add the following lines inside the application tag in *AndroidManifest.xml* file
```
<meta-data
            android:name="com.google.android.gms.ads.APPLICATION_ID"
            android:value="{admob-app-id}"/>
```

### Interstitial Ads

1. To create a new Interstitial Ad, call the *newInterstitialAd* method as shown

```
newInterstitialAd(adId: String, activity: AppCompatActivity,screen: String, closeListener: AdCloseListener): InterstitialAd
```
return type : InterstitialAd


2. To load an Interstitial Ad, call the *loadInterstitial* method as shown

```
loadInterstitial(mInterstitialAd: InterstitialAd)
```
#### Example
```
 ad = newInterstitialAd(resources.getString(R.string.test_interstitial), this@MainActivity, "app_exit", this)
 loadInterstitial(ad)
 if (ad.isLoaded) {
    ad.show()
  }
```

### Native Ads

#### 1. Template Native Ad for corresponding activity/fragment
1. Add the following lines inside the layout of a corresponding activity/fragment
```
<com.appyhigh.mylibrary.ads.TemplateView
        android:id="@+id/template_ad_small"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:gnt_template_type="@layout/gnt_medium_template_view" />
```

2. To load the template of Native Ad, call the *loadTemplateNativeAd* method as shown
```
loadTemplateNativeAd(context: Context, adId: String, templateView: TemplateView, screen: String)
```
#### Example
```
loadTemplateNativeAd(context = this, id = getString(R.string.test_native_id), templateView = template_ad_small, "main_screen")
```
#### 2. UnifiedNativeAd
1. To load the Unified Native Ad, call the *loadUnifiedNativeAd* method as shown
```
loadUnifiedNativeAd(context: Context, adId: String, nativeAdArea: LinearLayout, screen: String)
```
#### Example
```
loadTemplateNativeAd(context = this, adId = getString(R.string.test_native_id), nativeAdArea = linear_layout_id, "main_screen")
```
## Dynamic Linking


## Apxor Events
