package com.example.lab3

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_image.*
import java.io.IOException

class ImageActivity : AppCompatActivity() {
    companion object {
        val IMAGE_KEY = "image_key"
        val NAME_KEY = "name_key"
    }

    lateinit var photoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(this.javaClass.simpleName, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        text.text = intent.getStringExtra(NAME_KEY)
        photoPath = intent.getStringExtra(IMAGE_KEY)

        setPhoto()
    }

    private fun setPhoto() {
        Log.d(this.javaClass.simpleName, "setPhoto")
        var bitmap: Bitmap = BitmapFactory.decodeFile(photoPath)
        try {
            val exif = ExifInterface(photoPath)
            val matrix = Matrix()
            val rotation: Int = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            val rotationInDegrees: Int = exifToDegrees(rotation)
            if (rotation != 0) {
                matrix.preRotate(rotationInDegrees.toFloat())
            }
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            Log.d(this.javaClass.simpleName, "after created bitmap")
        } catch (ex: IOException) {
            Log.e(this.javaClass.simpleName, "Failed to get Exif data", ex)
        }
        imageView.setImageBitmap(bitmap)
    }

    private fun exifToDegrees(exifOrientation: Int): Int {
        Log.d(this.javaClass.simpleName, "exifToDegrees")
        return when (exifOrientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }
}
