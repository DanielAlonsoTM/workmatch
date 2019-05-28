package cl.ponceleiva.workmatch.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import android.view.View


fun logD(tag: String, message: String) {
    Log.w(tag, message)
}

fun logE(tag: String, message: String) {
    Log.e(tag, message)
}

fun toastMessage(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

//private fun hideSystemUI() {
//    // Enables regular immersive mode.
//    // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
//    // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//    window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
//            // Set the content to appear under the system bars so that the
//            // content doesn't resize when the system bars hide and show.
//            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//            // Hide the nav bar and status bar
//            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//            or View.SYSTEM_UI_FLAG_FULLSCREEN)
//}
