package com.meenbeese.chronos.data.preference

import android.os.Build
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import android.Manifest
import com.meenbeese.chronos.data.PreferenceEntry
import com.meenbeese.chronos.ui.screens.FileChooserScreen
import com.meenbeese.chronos.ui.screens.FileChooserType
import com.meenbeese.chronos.ui.views.PreferenceItem
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings


/**
 * A preference item that allows the user to select
 * an image from a file (the resulting preference
 * contains a valid image path / URI).
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ImageFilePreference(
    preference: PreferenceEntry.StringPref,
    @StringRes title: Int,
    @StringRes description: Int,
    modifier: Modifier = Modifier,
    onFileChosen: () -> Unit = {}
) {
    val context = LocalContext.current
    var showChooser by remember { mutableStateOf(false) }
    var showPermissionDialog by remember { mutableStateOf(false) }

    val requiredPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val permissionState = rememberPermissionState(requiredPermission)

    // Khi người dùng nhấn vào preference
    PreferenceItem(
        title = stringResource(id = title),
        description = stringResource(id = description),
        onClick = {
            when {
                permissionState.status.isGranted -> {
                    showChooser = true
                }
                permissionState.status.shouldShowRationale -> {
                    permissionState.launchPermissionRequest()
                }
                else -> {
                    // Người dùng đã từ chối và chọn "Không hỏi lại"
                    showPermissionDialog = true
                }
            }
        },
        modifier = modifier
    )

    // Mở màn hình chọn ảnh
    if (showChooser) {
        FileChooserScreen(
            type = FileChooserType.IMAGE,
            preference = preference,
            onFileChosen = { _, _ ->
                Toast.makeText(context, "Ảnh nền đã được chọn", Toast.LENGTH_SHORT).show()
                showChooser = false
                onFileChosen()
            },
            onDismiss = { showChooser = false }
        )
    }

    // Hiển thị dialog nếu người dùng đã từ chối vĩnh viễn
    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text("Yêu cầu quyền truy cập ảnh") },
            text = {
                Text("Ứng dụng cần quyền truy cập ảnh để chọn ảnh nền. Vui lòng cấp quyền trong phần Cài đặt.")
            },
            confirmButton = {
                TextButton(onClick = {
                    context.openAppSettings()
                    showPermissionDialog = false
                }) {
                    Text("Mở Cài đặt")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}

fun Context.openAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", packageName, null)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}



