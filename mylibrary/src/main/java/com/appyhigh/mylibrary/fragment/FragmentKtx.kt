
package com.appyhigh.mylibrary.fragment

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.appyhigh.mylibrary.activity.toast


inline fun <reified T : Activity> Fragment.openActivity() = startActivity(Intent(activity, T::class.java))

inline fun <reified T : Activity> Fragment.openActivity(requestCode: Int) = startActivityForResult(Intent(activity, T::class.java), requestCode)

inline fun <reified T : Service> Fragment.openService() = activity?.startService(Intent(activity, T::class.java))

inline fun <reified T : Service> Fragment.openService(sc: ServiceConnection, flags: Int = Context.BIND_AUTO_CREATE) = activity?.bindService(Intent(activity, T::class.java), sc, flags)


fun Fragment.toast(resourceId: Int, length: Int = Toast.LENGTH_SHORT) {
    activity?.toast(resourceId, length)
}

fun Fragment.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    activity?.toast(message, length)
}

inline fun Fragment.fragmentTransaction(function: FragmentTransaction.() -> FragmentTransaction) {
    fragmentManager?.beginTransaction()
            ?.function()
            ?.commit()
}