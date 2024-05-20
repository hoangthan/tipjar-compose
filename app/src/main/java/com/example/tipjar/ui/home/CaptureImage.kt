package com.example.tipjar.ui.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberImagePainter
import com.example.tipjar.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Preview
@Composable
fun CaptureImageFromCamera(
    onDone: (String?) -> Unit = {},
    onBack: () -> Unit = {},
) {
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(context, context.packageName + ".provider", file)
    var capturedImageUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        capturedImageUri = uri
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Can not access camera", Toast.LENGTH_SHORT).show()
        }
    }

    //Requesting permission to use camera
    val permissionCheckResult =
        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

    Box(modifier = Modifier.fillMaxSize()) {
        if (capturedImageUri.path?.isNotEmpty() != true) {
            Text(
                text = stringResource(id = R.string.take_a_photo_of_your_bill),
                modifier = Modifier.align(Alignment.Center)
            )

            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .height(48.dp)
                .clip(RoundedCornerShape(16.dp))
                .align(Alignment.BottomCenter), onClick = {
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(uri)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }) {
                Text(text = stringResource(id = R.string.take_a_photo))
            }
        } else {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                painter = rememberImagePainter(capturedImageUri.toString()),
                contentDescription = "BillImage",
                contentScale = ContentScale.FillBounds,
            )

            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .height(48.dp)
                .clip(RoundedCornerShape(16.dp))
                .align(Alignment.BottomCenter),
                onClick = {
                    onDone(capturedImageUri.path)
                }) {
                Text(text = stringResource(id = R.string.save_payment))
            }
        }

        Image(
            modifier = Modifier
                .align(Alignment.TopStart)
                .clickable { onBack() }
                .padding(16.dp),
            painter = painterResource(id = R.drawable.abc_vector_test),
            contentDescription = "ImageBack",
            colorFilter = ColorFilter.tint(Color.Black)
        )
    }
}

private fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyy_MM_dd_HH:mm:ss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(imageFileName, ".jpg", externalCacheDir)
    return image
}
