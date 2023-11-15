package com.cannonballapps.notechaser.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cannonballapps.notechaser.R
import javax.inject.Inject

class PermissionsManager @Inject constructor() {

    fun isRecordAudioPermissionGranted(context: Context) =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO,
        ).isGranted()

    fun requestRecordAudioPermission(activity: Activity, context: Context) {
        val micPermissionCode = 1
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)) {
            AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.microphonePermission_title))
                .setMessage(context.getString(R.string.microphonePermission_message))
                .setPositiveButton(context.getString(R.string.ok)) { _, _ ->
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.RECORD_AUDIO),
                        micPermissionCode,
                    )
                }
                .setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                .create().show()
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                micPermissionCode,
            )
        }
    }

    private fun Int.isGranted() =
        this == PackageManager.PERMISSION_GRANTED
}
