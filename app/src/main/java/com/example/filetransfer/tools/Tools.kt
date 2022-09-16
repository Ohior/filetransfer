package com.example.filetransfer.tools

import android.Manifest.permission
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.preference.PreferenceManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import com.example.filetransfer.R
import com.google.android.material.snackbar.Snackbar

object Tools {
    private lateinit var sharedPreferences: SharedPreferences


    fun debugMessage(message: String, tag: String = "DEBUG MESSAGE") {
        Log.e(tag, message)
    }

    fun goToFragment(fragmentManager: FragmentManager, containerId: Int, fragment: Fragment) {
        fragmentManager.beginTransaction()
            .replace(containerId, fragment).addToBackStack(null).commit()
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun requestForPermissions(context: Context, activity: Activity): Boolean {
        return if (checkForReadExternalStoragePermission(context)) {
            true
        } else {
            requestForPermission(activity)
            false
        }
    }

    fun navigateFragmentTo(screen_view: View, nav_id: Int) {
        Navigation.findNavController(screen_view)
            .navigate(nav_id)
    }

    private fun requestForPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(permission.READ_EXTERNAL_STORAGE),
            Constant.IMAGE_PERMISSION_REQUEST_CODE
        )
    }

    fun checkForReadExternalStoragePermission(context: Context): Boolean {
        val result = ContextCompat.checkSelfPermission(context, permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun popUpWindow(
        context: Context,
        view: View,
        gravity: Int = Gravity.BOTTOM,
        lambda: ((view: View) -> Unit)
    ) {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = layoutInflater.inflate(R.layout.popup_layout, null)
        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )
        popupWindow.showAtLocation(view, gravity, 0, 0)
        lambda(popupView)
    }

}