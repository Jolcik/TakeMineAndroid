package com.pkostrzenski.takemine.ui.photo_picker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import com.pkostrzenski.takemine.ui.base.BaseActivity
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

abstract class PhotoPickerBaseActivity : BaseActivity() {

    var currentPhotoPath: String? = null

    protected fun pickPhoto(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )

            if (permissions.any { checkSelfPermission(it) == PackageManager.PERMISSION_DENIED }){
                //show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE)
            }
            else pickPhotoFromDevice() //permission already granted

        }
        else pickPhotoFromDevice() //system OS is < Marshmallow
    }

    private fun pickPhotoFromDevice(){
        val pickIntent = Intent()
        pickIntent.type = "image/*"
        pickIntent.action = Intent.ACTION_GET_CONTENT

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile = try {
            createImageFile()
        } catch (e: IOException) {
            null
        }

        val chooser = Intent.createChooser(pickIntent, "Jak chcesz dodać zdjęcie?")

        photoFile?.let { file -> // if photoFile is created, add camera intent to chooser
            val photoUri = FileProvider.getUriForFile(
                this,
                "com.example.android.fileprovider",
                file
            )
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
        }

        startActivityForResult(chooser, PICK_IMAGE_REQUEST_CODE)
    }

    protected fun convertResultDataToByteArray(resultData: Intent?): ByteArray?{
        resultData?.data?.let { photoUri ->          // photo is from gallery
            return convertUriToByteArray(photoUri)
        }

        currentPhotoPath?.let { path ->              // photo is from camera
            val photoUri = Uri.parse(path)
            return convertUriToByteArray(photoUri)
        }

        return ByteArray(1) // return empty ByteArray
    }

    private fun convertUriToByteArray(photoUri: Uri): ByteArray? {
        val photoInputStream = contentResolver.openInputStream(photoUri)
        return photoInputStream?.let { stream ->
            stream.buffered().use { it.readBytes() }
        }
    }

    // currently unused, later it can be used to compress photos
    // in order to do that use BitmapFactory.decodeByteArray()
    // then retrieve Bitmap & compress it with this function
    private fun convertBitmapToByteArray(photoBitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).also {
            currentPhotoPath = "file:" + it.absolutePath
        }
    }

    companion object {
        const val PICK_IMAGE_REQUEST_CODE = 1000
        const val PERMISSION_CODE = 1001
    }

}