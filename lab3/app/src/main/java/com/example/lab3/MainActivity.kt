package com.example.lab3

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v4.content.FileProvider
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    var name: String = ""

    val REQUEST_IMAGE_CAPTURE = 1
    var currentPhotoPath: String = ""

    val CAMERA_REQUEST_CODE = 100

    companion object {
        val KEY_NAME = "key_name"
        val KEY_PATH = "key_path"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(this.javaClass.simpleName,"onCreate")
        button.setOnClickListener { v ->
            if (editText.text.isEmpty()) {
                Snackbar.make(v, "Заполните поле именем", Snackbar.LENGTH_LONG).show()
            } else {
                name = editText.text.toString()
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    takePictureIntent.resolveActivity(packageManager)?.also {
                        val photoFile: File? = try {
                            createImageFile()
                        } catch (ex: IOException) {
                            Snackbar.make(v, "Ошибка при создании файла фотографии", Snackbar.LENGTH_LONG).show()
                            null
                        }

                        //API:23
                        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            val perm = arrayOf(android.Manifest.permission.CAMERA)
                            requestPermissions(perm, CAMERA_REQUEST_CODE)
                        }

                        photoFile?.also {
                            val photoURI: Uri = FileProvider.getUriForFile(
                                this,
                                "com.example.android.fileprovider",
                                it
                            )
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                        }
                    }

                }

            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        Log.d(this.javaClass.simpleName, "createImageFile")
        val timeStamp: String = SimpleDateFormat("yyyyMMdd__HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(this.javaClass.simpleName, "onActivityResult")
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Intent(applicationContext, ImageActivity::class.java).also {
                it.putExtra(ImageActivity.IMAGE_KEY, currentPhotoPath)
                it.putExtra(ImageActivity.NAME_KEY, name)
                startActivity(it)
            }
//            var intent_activity = Intent(applicationContext, ImageActivity::class.java)
//            intent_activity.putExtra(ImageActivity.IMAGE_KEY, currentPhotoPath)
//            intent_activity.putExtra(ImageActivity.NAME_KEY, name)
//            startActivity(intent_activity)
        }
        else if (resultCode != Activity.RESULT_OK){
            Log.d(this.javaClass.simpleName, "result code != ok")
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        Log.d(this.javaClass.simpleName, "onSaveInstanceState")
        outState!!.putString(KEY_NAME, name)
        outState.putString(KEY_PATH, currentPhotoPath)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        Log.d(this.javaClass.simpleName, "onRestoreInstanceState")
        super.onRestoreInstanceState(savedInstanceState)
        name = savedInstanceState!!.getString(KEY_NAME).orEmpty()
        currentPhotoPath = savedInstanceState.getString(KEY_PATH).orEmpty()
    }
}
