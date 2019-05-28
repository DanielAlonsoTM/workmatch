package cl.ponceleiva.workmatch.utils

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import android.view.View
import android.view.Window
import android.view.WindowManager
import cl.ponceleiva.workmatch.R


fun logD(tag: String, message: String) {
    Log.w(tag, message)
}

fun logE(tag: String, message: String) {
    Log.e(tag, message)
}

fun toastMessage(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun changeColorNotificationBar(context: Context, window: Window) {
    // clear FLAG_TRANSLUCENT_STATUS flag:
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    // finally change the color
    window.setStatusBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
}
