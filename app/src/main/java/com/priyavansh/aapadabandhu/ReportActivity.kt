package com.priyavansh.aapadabandhu

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.exifinterface.media.ExifInterface
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.priyavansh.aapadabandhu.databinding.ActivityReportBinding
import com.priyavansh.aapadabandhu.models.Report
import com.priyavansh.aapadabandhu.models.User
import com.priyavansh.aapadabandhu.utils.DISASTER_IMAGES_FOLDER
import com.priyavansh.aapadabandhu.utils.REPORTS
import com.priyavansh.aapadabandhu.utils.USER_NODE
import com.priyavansh.aapadabandhu.utils.uploadImage
import java.io.IOException
import java.util.UUID
import android.location.Location
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class ReportActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityReportBinding.inflate(layoutInflater)
    }
    private lateinit var report: Report // Declare report here
    private lateinit var user: User
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImage(uri, DISASTER_IMAGES_FOLDER) {
                if (it != null) {
                    report.image = it
                    binding.takeImage.setImageURI(uri)
                }
            }
        }
    }
    private var imageUri: Uri? = null // Declare imageUri here
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        report = Report()
        user = User()
        val userID = Firebase.auth.currentUser!!.uid
        binding.uploadImage.setOnClickListener {
            launcher.launch("image/*")
        }

        binding.typeEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                // Move focus to locationEditText
                binding.locationEditText.requestFocus()
                true
            } else {
                false
            }
        }

        binding.locationEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                // Move focus to descriptionEditText
                binding.descriptionEditText.requestFocus()
                true
            } else {
                false
            }
        }

        binding.descriptionEditText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Hide the keyboard for the last field
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                true
            } else {
                false
            }
        }


        Firebase.firestore.collection(USER_NODE)
            .document(userID)
            .get().addOnSuccessListener {
                user =it.toObject<User>()!!
                report.userName = user.name
                report.userEmail = user.email
            }
        binding.back.setOnClickListener {
            finish()
        }

        binding.takeImage.setOnClickListener {
            imageUri = createImageUri()
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(cameraIntent, pic_id)
        }
        binding.captureImage.setOnClickListener {
            imageUri = createImageUri()
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(cameraIntent, pic_id)
        }

        startFetchingLocation()

        binding.reportBtn.setOnClickListener {
            val type = binding.type.editText?.text.toString()
            val location = binding.location.editText?.text.toString()
            val description = binding.description.editText?.text.toString()
            if ((type == "") or (location == "") || description == "") {
                Toast.makeText(
                    this@ReportActivity, "Please fill required information", Toast.LENGTH_SHORT
                ).show()
            } else{
                val reportID = UUID.randomUUID().toString()
                report.type = type
                report.location = location
                report.description = description
                report.time = System.currentTimeMillis().toString()
                report.userID = userID

                val firestore = Firebase.firestore

                // Save to the user's sub-collection
                firestore.collection(USER_NODE)
                    .document(userID)
                    .collection("Reports")
                    .document(reportID)
                    .set(report)
                    .addOnFailureListener {
                        Toast.makeText(this@ReportActivity, "Failed to save report: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                // Save to the global Reports collection
                firestore.collection(REPORTS)
                    .document(reportID)
                    .set(report)
                    .addOnSuccessListener {
                        Toast.makeText(this@ReportActivity, "Help will be reaching soon!!", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this@ReportActivity, "Failed to save report globally: ${it.localizedMessage}", Toast.LENGTH_LONG).show()
                    }

                finish()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pic_id && resultCode == RESULT_OK) {
            imageUri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                val orientation = getOrientation(it)
                val rotatedBitmap = rotateBitmap(bitmap, orientation)
                binding.takeImage.setImageBitmap(rotatedBitmap)
            }
        }
    }

    private fun startFetchingLocation() {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted
            getCurrentLocation(this) { location ->
                if (location != null) {
                    report.lat = location.first
                    report.long = location.second
                    Log.d("Location", "Latitude: ${report.lat}, Longitude: ${report.long}")
                    // Use the latitude and longitude as needed
                } else {
                    Log.e("LocationError", "Could not fetch location")
                }
            }
        } else {
            // Request permission
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    fun getCurrentLocation(context: Context, onResult: (Pair<Float, Float>?) -> Unit) {
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        try {
            fusedLocationClient.lastLocation.addOnCompleteListener { task: Task<Location> ->
                if (task.isSuccessful && task.result != null) {
                    val location = task.result
                    onResult(Pair(location.latitude.toFloat(), location.longitude.toFloat()))
                } else {
                    Log.e("LocationError", "Failed to get location")
                    onResult(null)
                }
            }
        } catch (e: SecurityException) {
            Log.e("LocationPermissionError", "Location permissions are not granted")
            onResult(null)
        }
    }

    private fun createImageUri(): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "image.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }

    @Throws(IOException::class)
    private fun getOrientation(uri: Uri): Int {
        val inputStream = contentResolver.openInputStream(uri)
        val exif = ExifInterface(inputStream!!)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        inputStream.close()
        return orientation
    }

    private fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap {
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            else -> return bitmap
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    companion object {
        private const val pic_id = 123
    }

    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    private fun checkLocationPermission() {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted, proceed to fetch location
            } else {
                Toast.makeText(this, "Location permission is required", Toast.LENGTH_SHORT).show()
            }
        }
    }

}