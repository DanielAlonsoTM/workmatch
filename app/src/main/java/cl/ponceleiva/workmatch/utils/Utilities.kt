package cl.ponceleiva.workmatch.utils

import android.content.Context
import android.content.res.Resources
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.util.Log
import android.widget.Toast
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
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

fun changeFullColorAppBar(context: Context, window: Window, actBar: ActionBar) {
    // clear FLAG_TRANSLUCENT_STATUS flag:
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    // finally change the color
    window.statusBarColor = ContextCompat.getColor(context, R.color.colorPrimary)

    val actionBar: ActionBar = actBar
//    actionBar.setBackgroundDrawable(resources.getDrawable(R.drawable.gradient_actionbar))

    actionBar.displayOptions = android.app.ActionBar.DISPLAY_SHOW_CUSTOM

    if (context.javaClass.name.contains("MainProfessionalActivity") || context.javaClass.name.contains("MainEmployerActivity")) {
        actionBar.setCustomView(R.layout.actionbar_custom)
    } else {
        actionBar.setCustomView(R.layout.actionbar_custom_others)
    }
}

fun changeColorInitialsViews(context: Context, window: Window) {
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = ContextCompat.getColor(context, R.color.colorPrimary)
}
