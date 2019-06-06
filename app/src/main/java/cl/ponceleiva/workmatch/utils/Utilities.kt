package cl.ponceleiva.workmatch.utils

import android.content.Context
import android.content.res.Resources
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.util.Log
import android.widget.Toast
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

fun changeFullColorAppBar(context: Context, window: Window, actBar: ActionBar, resoruces: Resources) {
    // clear FLAG_TRANSLUCENT_STATUS flag:
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    // finally change the color
    window.setStatusBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))

    val actionBar: ActionBar = actBar
    actionBar.setBackgroundDrawable(resoruces.getDrawable(R.drawable.gradient_actionbar))
}

fun changeColorInitialsViews(context: Context, window: Window) {
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.setStatusBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
}
