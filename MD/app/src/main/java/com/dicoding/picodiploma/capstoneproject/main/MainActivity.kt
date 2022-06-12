package com.dicoding.picodiploma.capstoneproject.main

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.dicoding.picodiploma.capstoneproject.databinding.ActivityMainBinding
import com.dicoding.picodiploma.capstoneproject.ml.Buma
import com.dicoding.picodiploma.capstoneproject.recycleview.BumaActivity
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var currentPhotoPath: String
    private lateinit var bitmap: Bitmap
    private var getFile: File? = null

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (!allPermissionGranted()) {
            Toast.makeText(
                this,
                "Tidak Mendapatkan Permission",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    private fun allPermissionGranted() = Companion.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.apply {
            binding.btnCamera.setOnClickListener { startCamera() }
            binding.btnGallery.setOnClickListener { startGallery() }
            binding.btnPredict.setOnClickListener { startPredict() }
        }

        setupSettings()
        setupMoveBuma()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        binding.previewImageView.setImageURI(data?.data)
        var uri: Uri? = data?.data

        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
    }

    fun getMax(array: FloatArray): Int {
        var ind = 0
        var min = 0.0f

        for (i in 0..4) {
            if (array[i] > min) {
                ind = i
                min = array[i]
            }
        }
        return ind
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createTempFile(application).also {
            val photoUri: Uri = FileProvider.getUriForFile(
                this@MainActivity,
                "com.dicoding.picodiploma.capstoneproject",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result = BitmapFactory.decodeFile(myFile.path)
            binding.previewImageView.setImageBitmap(result)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@MainActivity)
            getFile = myFile
            binding.previewImageView.setImageURI(selectedImg)
        }
    }

    private fun startPredict() {
        binding.btnPredict.setOnClickListener(View.OnClickListener {
            if (getFile!=null){
                val reduceFile = reduceFileImage(getFile as File)//dipanggil fungsi pada utils untuk reduce file image
                bitmap = BitmapFactory.decodeFile(reduceFile.path)
                val fileName = "label.txt"
                val inputString =
                    application.assets.open(fileName).bufferedReader().use { it.readText() }
                var townList = inputString.split("\n")

                var resized: Bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true)
                val model = Buma.newInstance(this)

                // Creates inputs for reference.
                val inputFeature0 =
                    TensorBuffer.createFixedSize(intArrayOf(1, 150, 150, 3), DataType.FLOAT32)
                var tensorImage = TensorImage(
                    DataType.FLOAT32
                )
                tensorImage.load(resized)

                var byteBuffer = tensorImage.buffer
                inputFeature0.loadBuffer(byteBuffer)

                // Runs model inference and gets result.
                val outputs = model.process(inputFeature0)
                val outputFeature0 = outputs.outputFeature0AsTensorBuffer

                var max = getMax(outputFeature0.floatArray)

                binding.tvSelect.text = townList[max].toString()

                // Releases model resources if no longer used.
                model.close()
            }else{
                Toast.makeText(this,"Masukan Gambar dahulu",Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun setupSettings() {
        binding.btnSetting.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
    private fun setupMoveBuma() {
        binding.btnHerbs.setOnClickListener {
            startActivity(Intent(this, BumaActivity::class.java))
        }
    }

}