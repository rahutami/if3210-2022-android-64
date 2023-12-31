package com.example.if3210_64

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.budiyev.android.codescanner.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback


class CheckIn : AppCompatActivity(), SensorEventListener {
    private lateinit var codeScanner: CodeScanner
    private lateinit var sensorManager: SensorManager
    private var text: TextView? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.check_in)

        // location
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 0
            )
        }

        val locationRequest = LocationRequest()
        val locationCallback = LocationCallback()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        val manager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }

        // Thermometer
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        text = findViewById<TextView>(R.id.temp_button)

        // QR Scanner
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        codeScanner = CodeScanner(this, scannerView)
        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not
        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            post(it.text)
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(
                    this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
            return@ErrorCallback
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

    }

    override fun onResume() {
        super.onResume()
        loadAmbientTemperature()
        codeScanner.startPreview()
    }

    override fun onPause() {
        sensorManager.unregisterListener(this)
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        if (sensorEvent.values.isNotEmpty()) {
            text?.text = sensorEvent.values[0].toString()
        }
    }

    private fun loadAmbientTemperature() {
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST)
        } else {
            Toast.makeText(this, "No Ambient Temperature Sensor !", Toast.LENGTH_LONG).show()
        }
    }

    private fun post(qr: String) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 0
            )
            return
        }
        fusedLocationClient.lastLocation
            .addOnCompleteListener(this) { location ->
                if (location.result != null) {
                    NetworkConfig().getService()
                        .createPost(
                            qr,
                            location.result!!.latitude,
                            location.result!!.longitude
                        )
                        .enqueue(object : Callback<QrCodeResponse> {
                            override fun onFailure(call: Call<QrCodeResponse>, t: Throwable) {
                                Toast.makeText(this@CheckIn, t.localizedMessage, Toast.LENGTH_SHORT)
                                    .show()
                            }

                            override fun onResponse(
                                call: Call<QrCodeResponse>,
                                response: retrofit2.Response<QrCodeResponse>
                            ) {
                                if (response.isSuccessful) {
                                    showDialog(
                                        response.body()!!.data.userStatus,
                                        response.body()!!.data.reason
                                    )
                                } else {
                                    Toast.makeText(this@CheckIn, "Bad Request", Toast.LENGTH_LONG)
                                        .show()
                                }
                            }
                        })
                } else {
                    // can not find location
                    buildAlertMessageNoGps()
                }
            }
    }

    fun showDialog(color: String, reason: String) {
        val fragmentManager = supportFragmentManager
        val newFragment = if (color == "green") {
            DialogCheckIn(true, reason)
        } else {
            DialogCheckIn(false, reason)
        }
        newFragment.show(fragmentManager, "dialog")
    }

    private fun buildAlertMessageNoGps() {
        var builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("OK",
                DialogInterface.OnClickListener { dialog, id ->
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    dialog.cancel()
                })
            .setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                })
        var alert = builder.create()
        alert.show()
    }
}

